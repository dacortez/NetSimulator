package dacortez.netSimulator.application.http;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public enum HttpAccept {
	HTML("text/html"), PNG("image/png");
	
	// Texto associado ao item.
	private String value;
	
	private HttpAccept(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}


