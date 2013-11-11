package dacortez.netSimulator.events;

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
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "*** InHost: " + args.toString();
	}
}
