package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.messages.Message;

public class TcpSegment extends Segment {

	public TcpSegment(Message message, Integer sourcePort,
			Integer destinationPort) {
		super(message, sourcePort, destinationPort);
		// TODO Auto-generated constructor stub
	}

}
