package dacortez.netSimulator.application.http;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public enum HttpMethod {
	GET("GET");
	
	// Texto associado ao item.
	private String value;
	
	private HttpMethod(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}


