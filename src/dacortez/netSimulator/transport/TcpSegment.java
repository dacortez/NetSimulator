package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class TcpSegment extends Segment {
	// Tamanho do cabeçalho TCP em bytes.
	private static final int HEADER_SIZE = 20;
	// Número de sequência.
	private int seqNumber;
	// Número de reconhecimento. 
	private int ackNumber;
	// Bit ACK.
	private boolean ack;
	// Bit SYN.
	private boolean syn;
	// Bit FIN.
	private boolean fin;
	
	public int getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	public int getAckNumber() {
		return ackNumber;
	}

	public void setAckNumber(int ackNumber) {
		this.ackNumber = ackNumber;
	}

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public boolean isSyn() {
		return syn;
	}

	public void setSyn(boolean syn) {
		this.syn = syn;
	}

	public boolean isFin() {
		return fin;
	}

	public void setFin(boolean fin) {
		this.fin = fin;
	}

	public TcpSegment(Message message, Integer sourcePort, Integer destinationPort) {
		super(message, sourcePort, destinationPort);
		ack = syn = fin = false;
	}
	
	@Override
	public int getNumberOfBytes() {
		return HEADER_SIZE + message.getNumberOfBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TCP_SEGMENT:\n");
		sb.append("Source port = ").append(sourcePort).append("\n");
		sb.append("Destination port = ").append(destinationPort).append("\n");
		sb.append("Destination port = ").append(destinationPort).append("\n");
		sb.append("Sequence number = ").append(seqNumber).append("\n");
		if (ack)
			sb.append("Acknowledgement number = ").append(ackNumber).append("\n");
		sb.append("Bit ACK = ").append(ack).append("\n");
		sb.append("Bit FIN = ").append(fin).append("\n");
		sb.append("Bit SYN = ").append(syn).append("\n");
		sb.append(message);
		return sb.toString();
	}
}
