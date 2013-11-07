package dacortez.netSimulator.application;

import dacortez.netSimulator.transport.UdpProvider;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class DnsServer extends Host implements ApplicationEvent {
	// Nome do servidor DNS.
	private String serverName;
	// Provedor de serviços UDP (estaria no kernel do "SO").
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
		udpProvider = new UdpProvider(hostInterface);
		udpProvider.addApplicationEventListener(this);
	}
	
	@Override
	public void applicationEventHandler(Message message) {
		System.out.println("Servidor DNS " + name + " recebeu mensagem:");
		System.out.println(message);
		System.out.println();
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
