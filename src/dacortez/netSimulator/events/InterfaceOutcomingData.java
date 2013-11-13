package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class InterfaceOutcomingData extends SimEvent {
	
	public InterfaceOutcomingData(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		sender.linkQueueing(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("INTERFACE_OUTCOMING_DATA:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
