package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class UdpSegment extends Segment {
	// Tamanho do cabeçalho UDP em bytes.
	private static final int HEADER_SIZE = 8;

	public UdpSegment(Message message, Integer sourcePort, Integer destinationPort) {
		super(message, sourcePort, destinationPort);
	}
	
	@Override
	public int getNumberOfBytes() {
		return HEADER_SIZE + message.getNumberOfBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UDP_SEGMENT (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append("Source port = ").append(sourcePort).append("\n");
		sb.append("Destination port = ").append(destinationPort).append("\n");
		sb.append(message);
		return sb.toString();
	}
}
