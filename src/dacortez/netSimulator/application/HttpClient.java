package dacortez.netSimulator.application;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class HttpClient extends Host {
	// Nome do cliente HTTP.
	private String clientName;
	
	public String getClientName() {
		return clientName;
	}

	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
