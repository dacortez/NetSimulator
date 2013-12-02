package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class UdpSegment extends Segment {
	// Tamanho do cabeçalho UDP em bytes.
	public static final int HEADER_SIZE = 8;

	public UdpSegment(Message message, Integer sourcePort, Integer destinationPort) {
		super(message, sourcePort, destinationPort);
	}
	
	public UdpSegment(Integer sourcePort, Integer destinationPort) {
		super(sourcePort, destinationPort);
	}

	@Override
	public int getNumberOfBytes() {
		if (message != null)
			return HEADER_SIZE + message.getNumberOfBytes();
		return HEADER_SIZE;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UDP_SEGMENT (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append("Source port = ").append(sourcePort).append("\n");
		sb.append("Destination port = ").append(destinationPort);
		if (message != null) sb.append("\n").append(message);
		return sb.toString();
	}
}
