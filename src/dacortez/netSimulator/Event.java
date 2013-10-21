package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.21
 */
public class Event {
	// Instante real a ser disparado evento. 
	private double time;
	// Ação a ser disparada pelo evento.
	private String action;
	
	public double getTime() {
		return time;
	}

	public String getAction() {
		return action;
	}
	
	public Event(double time, String action) {
		this.time = time;
		this.action = action;
	}
	
	@Override
	public String toString() {
		return "At " + time + " '" + action + "'";
	}
}
