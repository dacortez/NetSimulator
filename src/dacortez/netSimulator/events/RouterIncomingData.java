package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;
import dacortez.netSimulator.network.RouterInterface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class RouterIncomingData extends SimEvent {
	
	public RouterIncomingData(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		RouterInterface ri = (RouterInterface) sender;
		ri.queueing(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ROUTER_INCOMING_DATA:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
