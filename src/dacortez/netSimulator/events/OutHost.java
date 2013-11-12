package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class OutHost extends SimEvent {
	
	public OutHost(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		sender.fireNetworkEvent(args);
	}
	
	@Override
	public String toString() {
		return "*** OutHost: " + args.toString();
	}
}
