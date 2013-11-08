package dacortez.netSimulator.application;

import dacortez.netSimulator.transport.UdpProvider;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class DnsServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	// Provedor de serviços UDP (estaria no kernel do "SO").
	private UdpProvider udpProvider;
	
	public UdpProvider getUdpProvider() {
		return udpProvider;
	}
	
	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
		udpProvider = new UdpProvider(this);
	}
	
	@Override
	public void receive(Message message) {
		System.out.println("Aplicação do servidor DNS " + serverName + " recebeu menssagem:");
		System.out.println(message);
		System.out.println("[PROCESSANDO]\n");
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
