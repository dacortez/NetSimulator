package dacortez.netSimulator.application;

import dacortez.netSimulator.Host;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class HttpServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}

	public HttpServer(String serverName) {
		super();
		this.serverName = serverName;
	}
}
