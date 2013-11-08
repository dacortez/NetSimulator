package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.06
 */
public class SimEvent {
	// Instante real quando será disparado o evento. 
	private double time;
	// Descrição da ação a ser disparada pelo evento.
	private String action;
	
	public double getTime() {
		return time;
	}

	public String getAction() {
		return action;
	}
	
	public SimEvent(double time, String action) {
		this.time = time;
		this.action = action;
	}
	
	@Override
	public String toString() {
		return "At " + time + " '" + action + "'";
	}
}
