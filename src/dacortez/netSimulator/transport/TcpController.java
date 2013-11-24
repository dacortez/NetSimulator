package dacortez.netSimulator.transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.20
 */
public class TcpController {
	// Processo sendo controlado por este controlador.
	private Process process;
	// Socket associado ao processo sendo controlado.
	private Socket socket;
	// Interface do host associado para poder enviar os segmentos criados.
	private HostInterface hostInterface;
	// Estado TCP em que se encontra o controlador.
	private TcpState state;
	// Mensagem vinda da aplicação que seseja enviar.
	private Message message;
	// Número inicial de sequência escolhido.
	private int initialSeqNumber;
	// Número de sequência atual.
	private int lastByteSent;
	// Número de reconhecimento atual.
	private int lastByteRead;
	// Lista de segmentos a serem enviados criados a partir da mensagem da aplicação. 
	private List<TcpSegment> sendBuffer;
	// Lista de segmentos recebidos.
	private List<TcpSegment> receiveBuffer;
	// Lista de números de sequência de pacotes já reconhecidos.
	private List<Integer> alreadyAckeds;
	// Número de sequencia do pacote mais velho ainda não reconhecido.
	private int sendBase;
	// Tamanho da janela de congestionamento.
	private int congestionWindow;
	// Limiar atual para o controle de congestionamento.
	private int threshold;
	// Número de segmentos de dados enviados até o momento.
	private int totalSent;
	// Indica se o controlador deve fechar a conexão TCP ao enviar dados.
	private boolean closeConnection;
	// Maximum segment size (bytes): tamanho máximo de dados que cabe em um segmento.
	public final static int MSS = 1460;
	// Tempo em que o "timer" associado ao segmento expira (em ms).
	public static final double TIMEOUT = 1000;
	// Valor máximo de número de sequência permitido na inicialização.
	public static final int MAX_INIT_SEQ_NUMBER = 500;
	// Valor inicial do limiar para o controle de congestionamento.
	public static final int INITIAL_THRESHOLD = 10;
	// Gerador de números aleatórios utilizados pelo controlador.
	private Random random;
	// PrintStream para impressão da janela de congestionamento.
	private PrintStream ps;
	// Contador utilizado na impressão da janela de congestionamento.
	private int count = 0;
	
	public void setCloseConnection(boolean closeConnection) {
		this.closeConnection = closeConnection;
	}
	
	public TcpController(Process process, HostInterface hostInterface, TcpState state) {
		this.process = process;
		this.hostInterface = hostInterface;
		this.state = state;
		socket = process.getSocket();
		congestionWindow = 1;
		threshold = INITIAL_THRESHOLD;
		totalSent = 0;
		closeConnection = false;
		random = new Random();
		closeConnection = (state == TcpState.LISTEN); 
		setupPrintStream();
	}
	
	private void setupPrintStream() {
		if (!Simulator.experimentMode) {
			String host = hostInterface.getServiceProvider().getHost().getName();
			String name = "cw_" + host + "_pid" + process.getPid(); 
			try {
				ps = new PrintStream(new File(name));
			} catch (FileNotFoundException e) {
				ps = null;
			}	
		}
	}

	public void receive(TcpSegment segment) {
		switch (state) {
		case LISTEN:
			listen(segment);
			break;
		case SYN_SENT:
			synSent(segment);
			break;
		case SYN_RCVD:
			synRcvd();
			break;
		case ESTABLISHED:
			established(segment);
			break;
		case FIN_WAIT_1:
			finWait1(segment);
			break;
		case FIN_WAIT_2:
			finWait2(segment);
			break;
		case LAST_ACK:
			lastAck(segment);
			break;
		default:
			break;
		}		
	}

	private void listen(TcpSegment segment) {
		initialSeqNumber = random.nextInt(MAX_INIT_SEQ_NUMBER);
		lastByteSent = initialSeqNumber;
		lastByteRead = segment.getSeqNumber() + 1;
		TcpSegment synAndAck = ack(segment);
		synAndAck.setSyn(true);
		state = TcpState.SYN_RCVD;
		hostInterface.send(synAndAck, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private TcpSegment ack(TcpSegment segment) {
		TcpSegment ack = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		ack.setAck(true);
		ack.setSeqNumber(lastByteSent++);
		ack.setAckNumber(lastByteRead);
		return ack;
	}
	
	private void synSent(TcpSegment segment) {
		sendAckAndSwitchToEstablished(segment);
		sendMessage();
	}
	
	private void sendAckAndSwitchToEstablished(TcpSegment segment) {
		lastByteRead = segment.getSeqNumber() + 1;
		allocateBuffers();
		state = TcpState.ESTABLISHED;
		hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private void synRcvd() {
		lastByteRead++;	
		allocateBuffers();
		state = TcpState.ESTABLISHED;
	}

	private void allocateBuffers() {
		sendBuffer = new ArrayList<TcpSegment>();
		receiveBuffer = new ArrayList<TcpSegment>();
		alreadyAckeds = new ArrayList<Integer>();
	}
	
	private void established(TcpSegment segment) {
		if (segment.isFin()) {
			sendAckAndSwitchToCloseWait(segment);
			sendFinAndSwitchToLastAck();
		}
		else if (segment.isAck())
			handleAck(segment);
		else
			handleNewData(segment);		
	}
	
	private void sendAckAndSwitchToCloseWait(TcpSegment segment) {
		state = TcpState.CLOSE_WAIT;
		lastByteRead++;
		hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private void sendFinAndSwitchToLastAck() {
		TcpSegment fin = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		fin.setFin(true);
		fin.setSeqNumber(lastByteSent++);
		state = TcpState.LAST_ACK;
		hostInterface.send(fin, socket.getSourceIp(), socket.getDestinationIp());
	}

	private void handleAck(TcpSegment ack) {
		lastByteRead++;	
		Integer y = ack.getAckNumber();
		if (y > sendBase) {
			updateAckedsList(ack);
			sendBase = y;
		}
		if (!connectionShouldBeClosed())
			updateCongestionWindowAndSendNexts();
	}

	private void updateAckedsList(TcpSegment ack) {
		for (TcpSegment sent: sendBuffer)
			if (sent.getSeqNumber() < ack.getAckNumber()) {
				int seqNumber = sent.getSeqNumber();
				if (!alreadyAckeds.contains(seqNumber))
					alreadyAckeds.add(seqNumber);
			}
	}

	private boolean connectionShouldBeClosed() {
		if (alreadyAckeds.size() == sendBuffer.size()) {
			if (closeConnection)
				sendFinAndSwitchToFinWait1();
			return true;
		}
		return false;
	}
	
	private void updateCongestionWindowAndSendNexts() {
		if (alreadyAckeds.size() == totalSent) {
			if (congestionWindow == 0)
				congestionWindow = 1;
			else if (congestionWindow < threshold) 
				congestionWindow *= 2;
			else 
				congestionWindow++;
			sendNexts();
		}
	}
	
	private void handleNewData(TcpSegment segment) {
		if (lastByteRead == segment.getSeqNumber()) {
			receiveBuffer.add(segment);
			lastByteRead += segment.getMessage().getNumberOfBytes();
			hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
			if (segment.isPsh()) {		
				ServiceProvider sp = hostInterface.getServiceProvider();
				sp.getHost().receive(assembled(), process);
			}
		}
		else
			if (Simulator.debugMode) System.out.println(notCorrectOrderMsg(segment));
	}
	
	private String notCorrectOrderMsg(TcpSegment segment) {
		StringBuilder sb = new StringBuilder();
		sb.append("! Host " + hostInterface.getIp());
		sb.append(" reconheceu dados fora de ordem.\n");
		sb.append("! Sequência esperada = " + lastByteRead).append("\n");
		sb.append("! Sequência recebida = " + segment.getSeqNumber()).append("\n");
		return sb.toString();
	}

	private Message assembled() {
		int size = 0;
		for (TcpSegment segment: receiveBuffer)
			size += segment.getMessage().getNumberOfBytes();
		byte[] data = new byte[size];
		int offset = 0;
		for (TcpSegment segment: receiveBuffer) {
			byte[] segmentData = segment.getMessage().toBytes();
			for (int i = 0; i < segmentData.length; i++)
				data[i + offset] = segmentData[i];
			offset += segmentData.length;
		}
		return new Message(data);
	}
	
	private void sendFinAndSwitchToFinWait1() {
		TcpSegment fin = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		fin.setFin(true);
		fin.setSeqNumber(lastByteSent++);
		state = TcpState.FIN_WAIT_1;
		hostInterface.send(fin, socket.getSourceIp(), socket.getDestinationIp());
	}

	private void finWait1(TcpSegment segment) {
		if (segment.isAck())
			state = TcpState.FIN_WAIT_2;
	}
	
	private void finWait2(TcpSegment segment) {
		if (segment.isFin()) {
			state = TcpState.TIME_WAIT;
			hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
			state = TcpState.CLOSED;
			if (Simulator.debugMode) System.out.println("### Processo fechado:\n" + process);
		}
	}
	
	private void lastAck(TcpSegment segment) {
		if (segment.isAck()) {
			state = TcpState.CLOSED;
			if (Simulator.debugMode) System.out.println("### Processo fechado:\n" + process);
		}
	}
	
	public void send(Message message) {
		this.message = message;
		if (state == TcpState.CLOSED) 
			sendSyn(); 
		else if (state == TcpState.ESTABLISHED)
			sendMessage();
	}
	
	public void sendSyn() {
		hostInterface.getServiceProvider().bindProcessSocket(process);		
		initialSeqNumber = random.nextInt(MAX_INIT_SEQ_NUMBER);
		lastByteSent = initialSeqNumber;
		TcpSegment syn = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		syn.setSeqNumber(lastByteSent++);
		syn.setSyn(true);
		state = TcpState.SYN_SENT;
		hostInterface.send(syn, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	public void sendMessage() {
		sendBase = lastByteSent;
		makeSegments();
		sendBuffer.get(sendBuffer.size() - 1).setPsh(true);	
		sendNexts();
	}
	
	private void makeSegments() {
		byte[] messageData = message.toBytes();
		int numberOfSegments = messageData.length / MSS;
		for (int i = 0; i < numberOfSegments; i++) {
			byte[] segmentData = new byte[MSS];
			for (int j = 0; j < MSS; j++)
				segmentData[j] = messageData[j + i * MSS];
			addToSendBuffer(segmentData);
			lastByteSent += MSS;
		}
		makeLastFrame(messageData, numberOfSegments);
	}

	private void makeLastFrame(byte[] messageData, int numberOfSegments) {
		int rest = messageData.length % MSS;
		if (rest > 0) {
			byte[] segmentData = new byte[rest];
			for (int j = 0 ; j < rest; j++)
				segmentData[j] = messageData[numberOfSegments * MSS + j];
			addToSendBuffer(segmentData);
			lastByteSent += rest;
		}
	}
	
	private void addToSendBuffer(byte[] data) {
		Message message = new Message(data);
		TcpSegment segment = new TcpSegment(message, socket.getSourcePort(), socket.getDestinationPort());
		segment.setSeqNumber(lastByteSent);
		sendBuffer.add(segment);
	}
	
	private void sendNexts() {	
		printCongestionWindow();
		for (int i = 0; i < congestionWindow; i++) {
			if (totalSent == sendBuffer.size()) break;
			TcpSegment segment = sendBuffer.get(totalSent++);
			hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
		}
	}

	private void printCongestionWindow() {
		if (ps != null) 
			ps.println((count++) + "\t" + congestionWindow + "\t" + threshold);
	}
		
	public void timeout(TcpSegment segment) {
		Integer seqNumber = segment.getSeqNumber();
		if (!alreadyAckeds.contains(seqNumber)) {
			if (Simulator.debugMode) System.out.println(retransmitingMsg(segment));
			hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
			if (congestionWindow > 0) {
				threshold = congestionWindow / 2;
				congestionWindow = 0;
			}
		}
		else
			if (Simulator.debugMode) System.out.println(alreadAckedMsg(segment));
	}
	
	private String retransmitingMsg(TcpSegment segment) {
		StringBuilder sb = new StringBuilder();
		sb.append("! TIMEOUT: " + hostInterface.getIp());
		sb.append(" retransmitindo o pacote ainda não reconhecido.\n");
		sb.append("! Número de sequência: ").append(segment.getSeqNumber()).append("\n");
		return sb.toString();
	}
	
	private String alreadAckedMsg(TcpSegment segment) {
		StringBuilder sb = new StringBuilder();
		sb.append("! TIMEOUT: " + hostInterface.getIp());
		sb.append(" já reconheceu o pacote.\n");
		sb.append("! Número de sequência: ").append(segment.getSeqNumber()).append("\n");
		return sb.toString();
	}
}
