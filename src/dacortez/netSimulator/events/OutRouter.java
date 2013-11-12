package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;
import dacortez.netSimulator.network.RouterInterface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class OutRouter extends SimEvent {
	
	public OutRouter(Interface sender, EventArgs args) {
		super(sender, args);
	}

	@Override
	public void fire() {
		RouterInterface ri = (RouterInterface) sender;
		ri.route(args);
	}
	
	@Override
	public String toString() {
		return "*** OutRouter: " + args.toString();
	}
}
