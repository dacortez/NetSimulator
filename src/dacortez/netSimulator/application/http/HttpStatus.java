package dacortez.netSimulator.application.http;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public enum HttpStatus {
	OK(200, "OK"), 
	BAD_REQUEST(400, "Bad Request"), 
	NOT_FOUND(404, "Not Found"), 
	INTERNAL(500, "Internal Server Error");

	// Código associada ao status da resposta HTTP.
	private int code;
	// Frase associada ao status da resposta HTTP.
	private String phrase;
	
	private HttpStatus(int code, String phrase) {
		this.code = code;
		this.phrase = phrase;
	}
	
	@Override
	public String toString() {
		return code + " " + phrase;
	}
}
