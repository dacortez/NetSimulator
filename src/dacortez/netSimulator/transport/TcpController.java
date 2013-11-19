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
	// Lista de segmentos a serem enviados criados a partir da mensagem da aplicação. 
	private List<TcpSegment> sendBuffer;
	// Lista de segmentos recebidos.
	private List<TcpSegment> receiveBuffer;
	// Maximum segment size (bytes): tamanho máximo de dados que cabe em um segmento.
	private final static int MSS = 1460;
	// Tempo em que o "timer" associado ao segmento expira (em ms).
	public static final double TIMEOUT = 500;
	// Valor máximo de número de sequência permitido na inicialização.
	private static final int MAX_INIT_SEQ_NUMBER = 1000;
	// Gerador de números aleatórios utilizados pelo controlador.
	private Random random;
	
	public TcpController(Process process, HostInterface hostInterface, TcpState state) {
		this.process = process;
		socket = process.getSocket();
		this.hostInterface = hostInterface;
		random = new Random();
		this.state = state;
	}
	
	public void send(Message message) {
		this.message = message;
		if (state == TcpState.CLOSED) 
			sendSyn(); 
		else if (state == TcpState.ESTABLISHED) {
			sendMessage();
			// Só deve fechar qando recebeu ack do último pacote.
			//if (process instanceof HttpServerProcess)
				//sendFinAndSwitchToFinWait1();
		}
	}
	
	public void sendSyn() {
		hostInterface.getServiceProvider().bindProcessSocket(process);		
		//currentSeqNumber = random.nextInt(MAX_INIT_SEQ_NUMBER);
		currentSeqNumber = 0;
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
		//currentSeqNumber = random.nextInt(MAX_INIT_SEQ_NUMBER);
		currentSeqNumber = 0;
		TcpSegment synAndAck = ack(segment);
		synAndAck.setSyn(true);
		state = TcpState.SYN_RCVD;
		hostInterface.send(synAndAck, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private TcpSegment ack(TcpSegment segment) {
		TcpSegment ack = new TcpSegment(socket.getSourcePort(), socket.getDestinationPort());
		ack.setAck(true);
		ack.setSeqNumber(currentSeqNumber++);
		setAckNumber(segment, ack);
		return ack;
	}

	private void setAckNumber(TcpSegment segment, TcpSegment ack) {
		Message message = segment.getMessage();
		if (message == null)
			ack.setAckNumber(segment.getSeqNumber() + 1);
		else
			ack.setAckNumber(segment.getSeqNumber() + message.getNumberOfBytes());
	}
	
	private void synSent(TcpSegment segment) {
		sendAckAndSwitchToEstablished(segment);
		sendMessage();
	}
	
	private void sendAckAndSwitchToEstablished(TcpSegment segment) {
		TcpSegment ack = ack(segment);
		allocateBuffers();
		state = TcpState.ESTABLISHED;
		hostInterface.send(ack, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	private void synRcvd() {
		allocateBuffers();
		state = TcpState.ESTABLISHED;
	}

	private void allocateBuffers() {
		sendBuffer = new ArrayList<TcpSegment>();
		receiveBuffer = new ArrayList<TcpSegment>();
	}
	
	private void established(TcpSegment segment) {
		if (segment.isFin()) {
			sendAckAndSwitchToCloseWait(segment);
			sendFinAndSwitchToLastAck();
		}
		else
			notFin(segment);
	}

	private void notFin(TcpSegment segment) {
		if (!segment.isAck()) {
			pushToBufferAndSendAck(segment);
		}
		else {
			// TODO
		}
		if (segment.isPsh()) {		
			// TODO
			ServiceProvider sp = hostInterface.getServiceProvider();
			sp.getHost().receive(assembled(), process);
		}
	}

	private void pushToBufferAndSendAck(TcpSegment segment) {
		receiveBuffer.add(segment);
		hostInterface.send(ack(segment), socket.getSourceIp(), socket.getDestinationIp());
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
		makeSegments();
		sendBuffer.get(sendBuffer.size() - 1).setPsh(true);
		for (TcpSegment segment: sendBuffer)
			hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
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

	}
}
