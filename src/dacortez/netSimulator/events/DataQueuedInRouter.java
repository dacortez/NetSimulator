package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;
import dacortez.netSimulator.network.RouterInterface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class DataQueuedInRouter extends SimEvent {
	
	public DataQueuedInRouter(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		RouterInterface ri = (RouterInterface) sender;
		ri.dequeueing(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DATA_QUEUED_IN_ROUTER:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
