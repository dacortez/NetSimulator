package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class OutboundData extends SimEvent {
	
	public OutboundData(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		sender.queuing(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("OUTBOUND_DATA:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
