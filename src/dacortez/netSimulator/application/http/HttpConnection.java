package dacortez.netSimulator.application.http;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public enum HttpConnection {
	OPEN("open"), CLOSE("close"), KEEP_ALIVE("keep alive");
	
	// Texto associado ao item.
	private String value;
	
	private HttpConnection(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}


