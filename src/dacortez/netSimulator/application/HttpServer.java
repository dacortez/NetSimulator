package dacortez.netSimulator.application;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.transport.TcpProvider;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class HttpServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	@SuppressWarnings("unused")
	private TcpProvider tcpProvider;
	
	public String getServerName() {
		return serverName;
	}

	public HttpServer(String serverName) {
		super();
		this.serverName = serverName;
	}
	
	@Override
	public void attach(Host host) {
		super.attach(host);
		tcpProvider = new TcpProvider(iface);
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
