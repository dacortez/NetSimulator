package dacortez.netSimulator.events;

import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.network.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class InHost extends SimEvent {
	
	public InHost(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		HostInterface hi = (HostInterface) sender;
		hi.receive(args);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("IN_HOST:\n");
		sb.append("Sender: ").append(sender).append("\n");
		sb.append(args);
		return sb.toString();
	}
}
