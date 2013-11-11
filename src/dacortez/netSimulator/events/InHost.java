package dacortez.netSimulator.events;

import dacortez.netSimulator.application.Host;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class InHost extends SimEvent {
	// Hospedeiro associado ao evento.
	@SuppressWarnings("unused")
	private Host host;
	
	public InHost(Host sender, EventArgs args) {
		super(args);
		host = sender;
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
