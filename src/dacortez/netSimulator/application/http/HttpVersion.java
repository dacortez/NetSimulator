package dacortez.netSimulator.application.http;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public enum HttpVersion {
	HTTP_10("HTTP/1.1"), HTTP_11("HTTP/1.0");
	
	// Texto associado ao item.
	private String value;
	
	private HttpVersion(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
