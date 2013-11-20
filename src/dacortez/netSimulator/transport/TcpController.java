package dacortez.netSimulator.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;
import dacortez.netSimulator.application.http.HttpServerProcess;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.20
 */
@SuppressWarnings("unused")
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
	// Número de sequência atual.
	private int currentSeqNumber;
	// Número de reconhecimento atual.
	private int currentAckNumber;
	// Lista de segmentos a serem enviados criados a partir da mensagem da aplicação. 
	private List<TcpSegment> sendBuffer;
	// Lista de segmentos recebidos.
	private List<TcpSegment> receiveBuffer;
	// Lista de números de sequência de pacotes já reconhecidos.
	private List<Integer> alreadyAckeds;
	// Número de sequencia do pacote mais velho ainda não reconhecido.
	private int sendBase;
	// Indica se o controlador deve fechar a conexão TCP ao enviar dados.
	private boolean closeConnection;
	// Maximum segment size (bytes): tamanho máximo de dados que cabe em um segmento.
	private final static int MSS = 1460;
	// Tempo em que o "timer" associado ao segmento expira (em ms).
	public static final double TIMEOUT = 5000;
	// Valor máximo de número de sequência permitido na inicialização.
	private static final int MAX_INIT_SEQ_NUMBER = 1000;
	// Gerador de números aleatórios utilizados pelo controlador.
	private Random random;
	
	public void setCloseConnection(boolean closeConnection) {
		this.closeConnection = closeConnection;
	}
	
	public TcpController(Process process, HostInterface hostInterface, TcpState state) {
		this.process = process;
		this.hostInterface = hostInterface;
		this.state = state;
		socket = process.getSocket();
		closeConnection = false;
		random = new Random();
		closeConnection = (state == TcpState.LISTEN); 
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
		currentSeqNumber = random.nextInt(MAX_INIT_SEQ_NUMBER);
		//currentSeqNumber = 0;
		TcpSegment syn = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		syn.setSeqNumber(currentSeqNumber++);
		syn.setSyn(true);
		state = TcpState.SYN_SENT;
		hostInterface.send(syn, socket.getSourceIp(), socket.getDestinationIp());
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
		currentSeqNumber = random.nextInt(MAX_INIT_SEQ_NUMBER);
		//currentSeqNumber = 0;
		currentAckNumber = segment.getSeqNumber() + 1;
		TcpSegment synAndAck = ack(segment);
		synAndAck.setSyn(true);
		state = TcpState.SYN_RCVD;
		hostInterface.send(synAndAck, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private TcpSegment ack(TcpSegment segment) {
		TcpSegment ack = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		ack.setAck(true);
		ack.setSeqNumber(currentSeqNumber++);
		ack.setAckNumber(currentAckNumber);
		return ack;
	}
	
	private void synSent(TcpSegment segment) {
		sendAckAndSwitchToEstablished(segment);
		sendMessage();
	}
	
	private void sendAckAndSwitchToEstablished(TcpSegment segment) {
		currentAckNumber = segment.getSeqNumber() + 1;
		allocateBuffers();
		state = TcpState.ESTABLISHED;
		hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private void synRcvd() {
		currentAckNumber++;	
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

	private void handleAck(TcpSegment ack) {
		currentAckNumber++;	
		Integer y = ack.getAckNumber();
		if (y > sendBase) {
			for (TcpSegment sent: sendBuffer)
				if (sent.getSeqNumber() <= ack.getAckNumber())
					alreadyAckeds.add(sent.getSeqNumber());
			sendBase = y;
		}
		if (sendBase >= message.getNumberOfBytes() + 2)
			if (closeConnection)
				sendFinAndSwitchToFinWait1();
	}
	
	private void handleNewData(TcpSegment segment) {
		if (currentAckNumber == segment.getSeqNumber()) {
			receiveBuffer.add(segment);
			currentAckNumber += segment.getMessage().getNumberOfBytes();
			hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
			if (segment.isPsh()) {		
				ServiceProvider sp = hostInterface.getServiceProvider();
				sp.getHost().receive(assembled(), process);
			}
		}
		else {
			System.out.println(notCorrectOrderMsg(segment));
		}
	}
	
	private String notCorrectOrderMsg(TcpSegment segment) {
		StringBuilder sb = new StringBuilder();
		sb.append("! Host " + hostInterface.getIp());
		sb.append(" reconheceu dados fora de ordem.\n");
		sb.append("! Sequência esperada = " + currentAckNumber).append("\n");
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
		fin.setSeqNumber(currentSeqNumber++);
		state = TcpState.FIN_WAIT_1;
		hostInterface.send(fin, socket.getSourceIp(), socket.getDestinationIp());
	}

	private void sendAckAndSwitchToCloseWait(TcpSegment segment) {
		state = TcpState.CLOSE_WAIT;
		currentAckNumber++;
		hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private void sendFinAndSwitchToLastAck() {
		TcpSegment fin = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		fin.setFin(true);
		fin.setSeqNumber(currentSeqNumber++);
		state = TcpState.LAST_ACK;
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
		}
	}
	
	private void lastAck(TcpSegment segment) {
		if (segment.isAck())
			state = TcpState.CLOSED;
	}
	
	public void sendMessage() {
		sendBase = currentSeqNumber;
		makeSegments();
		sendBuffer.get(sendBuffer.size() - 1).setPsh(true);	
		for (TcpSegment segment: sendBuffer)
			hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
		//if (process instanceof HttpServerProcess)
		//	throw new RuntimeException();
	}
	
	private void makeSegments() {
		byte[] messageData = message.toBytes();
		int numberOfSegments = messageData.length / MSS;
		for (int i = 0; i < numberOfSegments; i++) {
			byte[] segmentData = new byte[MSS];
			for (int j = 0; j < MSS; j++)
				segmentData[j] = messageData[j + i * MSS];
			addToSendBuffer(segmentData);
			currentSeqNumber += MSS;
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
			currentSeqNumber += rest;
		}
	}
	
	private void addToSendBuffer(byte[] data) {
		Message message = new Message(data);
		TcpSegment segment = new TcpSegment(message, socket.getSourcePort(), socket.getDestinationPort());
		segment.setSeqNumber(currentSeqNumber);
		sendBuffer.add(segment);
	}
	
	public void timeout(TcpSegment segment) {
		Integer seqNumber = segment.getSeqNumber();
		if (!alreadyAckeds.contains(seqNumber)) {
			System.out.println(retransmitingMsg(segment));
			hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
		}
		else {
			System.out.println(hostInterface.getIp());
			System.out.println("(Número de sequência = " + segment.getSeqNumber() + " já reconhecido).\n");
		}
	}
	
	private String retransmitingMsg(TcpSegment segment) {
		StringBuilder sb = new StringBuilder();
		sb.append("! Host " + hostInterface.getIp());
		sb.append(" retransmitindo pacote ainda não reconhecido.\n");
		sb.append("! Número de sequência: ").append(segment.getSeqNumber()).append("\n");
		return sb.toString();
	}
}
