package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Router;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class InRouter extends SimEvent {
	// Roteador associado ao evento.
	@SuppressWarnings("unused")
	private Router router;
	
	public InRouter(Router sender, EventArgs args) {
		super(args);
		router = sender;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "*** InRouter: " + args.toString();
	}
}
