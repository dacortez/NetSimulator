package dacortez.netSimulator.events;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public abstract class SimEvent {
	// Argumentos associado ao evento (datagram e instante da simulação).
	protected EventArgs args;
	
	public EventArgs getEventArgs() {
		return args;
	}
	
	public SimEvent(EventArgs args) {
		this.args = args;
	}
	
	public abstract void fire();
}
