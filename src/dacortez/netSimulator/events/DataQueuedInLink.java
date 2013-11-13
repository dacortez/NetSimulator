package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

public class DataQueuedInLink extends SimEvent {

	public DataQueuedInLink(Interface sender, EventArgs args) {
		super(sender, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fire() {
		sender.linkDequeueing(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DATA_QUEUED_IN_LINK:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
