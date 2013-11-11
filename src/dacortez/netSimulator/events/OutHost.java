package dacortez.netSimulator.events;

import dacortez.netSimulator.application.Host;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class OutHost extends SimEvent {
	// Hospedeiro associado ao evento.
	@SuppressWarnings("unused")
	private Host host; 
	
	public OutHost(Host sender, EventArgs args) {
		super(args);
		host = sender;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "*** OutHost: " + args.toString();
	}
}
