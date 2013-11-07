package dacortez.netSimulator.application;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.network.Datagram;
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
	}
	
	@Override
	public void attach(Host host) {
		super.attach(host);
		udpProvider = new UdpProvider(iface);
	}
	
	@Override
	public void networkEventHandler(Interface sender, Datagram data) {
		// TODO
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
