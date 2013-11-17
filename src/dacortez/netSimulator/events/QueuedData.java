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
		return "QUEUED_DATA:\n" + super.toString();
	}
}
