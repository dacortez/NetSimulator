package dacortez.netSimulator.events;

import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.network.Interface;

public class InterfaceIncomingData extends SimEvent {

	public InterfaceIncomingData(Interface sender, EventArgs args) {
		super(sender, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fire() {
		HostInterface hi = (HostInterface) sender;
		hi.receive(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("INTERFACE_INCOMING_DATA:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
