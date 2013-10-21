package dacortez.netSimulator.application;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class DnsServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
