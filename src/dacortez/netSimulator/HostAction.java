package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class HostAction {
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
	
	public HostAction(double time, String action) {
		this.time = time;
		this.action = action.trim();
	}
	
	public boolean isFinish() {
		return action.toLowerCase().contentEquals("finish");
	}
	
	public boolean isGet() {
		String method = getMethod();
		if (method != null)
			return method.toLowerCase().contentEquals("get");
		return false;
	}
	
	public String getHost() {
		return getField(1);
	}
	
	public String getMethod() {
		return getField(2);
	}
	
	public String getTarget() {
		return getField(3);
	}
	
	public String getResource() {
		return getField(4);
	}
	
	public String getField(int field) {
		String[] fields = action.split("\\s+");
		if (fields.length >= field)
			return fields[field - 1];
		return null;
	}
	
	@Override
	public String toString() {
		return "At " + time + " '" + action + "'";
	}
}
