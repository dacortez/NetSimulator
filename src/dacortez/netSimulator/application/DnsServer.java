package dacortez.netSimulator.application;

import dacortez.netSimulator.transport.UdpProvider;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class DnsServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	// Provedor de serviços UDP (estaria no kernel do "SO").
	@SuppressWarnings("unused")
	private UdpProvider udpProvider;
	
	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
		udpProvider = new UdpProvider();
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
