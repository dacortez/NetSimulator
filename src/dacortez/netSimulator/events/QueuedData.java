package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

public class QueuedData extends SimEvent {

	public QueuedData(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		sender.dequeuing(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("QUEUED_DATA:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
