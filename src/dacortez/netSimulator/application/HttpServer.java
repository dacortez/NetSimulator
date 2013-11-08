package dacortez.netSimulator.application;

import dacortez.netSimulator.transport.TcpProvider;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	// Provedor de serviços TCP (estaria no kernel do "SO").
	private TcpProvider tcpProvider;
	
	public TcpProvider getTcpProvider() {
		return tcpProvider;
	}
	
	public String getServerName() {
		return serverName;
	}

	public HttpServer(String serverName) {
		super();
		this.serverName = serverName;
		tcpProvider = new TcpProvider(this);
	}
	
	@Override
	public void receive(Message message) {
		System.out.println("Aplicação do servidor HTTP " + serverName + " recebeu menssagem:");
		System.out.println(message);
		System.out.println("[PROCESSANDO]\n");
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
