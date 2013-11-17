package dacortez.netSimulator.events;

import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.network.Interface;

public class InboundData extends SimEvent {

	public InboundData(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		HostInterface hi = (HostInterface) sender;
		hi.receive(args);
	}
	
	@Override
	public String toString() {
		return "INBOUND_DATA:\n" + super.toString();
	}
}
