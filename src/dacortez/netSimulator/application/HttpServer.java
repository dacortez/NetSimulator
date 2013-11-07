package dacortez.netSimulator.application;

import dacortez.netSimulator.transport.TcpProvider;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class HttpServer extends Host implements ApplicationEvent {
	// Nome do servidor DNS.
	private String serverName;
	// Provedor de serviços TCP (estaria no kernel do "SO").
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
		tcpProvider = new TcpProvider(hostInterface);
		tcpProvider.addApplicationEventListener(this);
	}
	
	@Override
	public void applicationEventHandler(Message message) {
		// TODO	
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
