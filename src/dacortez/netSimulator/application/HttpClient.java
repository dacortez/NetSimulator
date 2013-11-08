package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.transport.TcpProvider;
import dacortez.netSimulator.transport.UdpProvider;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpClient extends Host {
	// Nome do cliente HTTP.
	private String clientName;
	// Provedor de serviços UDP (estaria no kernel do "SO").
	private UdpProvider udpProvider;
	// Provedor de serviços TCP (estaria no kernel do "SO").
	private TcpProvider tcpProvider;
	
	public String getClientName() {
		return clientName;
	}
	
	public UdpProvider getUdpProvider() {
		return udpProvider;
	}
	
	public TcpProvider getTcpProvider() {
		return tcpProvider;
	}

	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
		udpProvider = new UdpProvider(this);
		tcpProvider = new TcpProvider(this);
	}
	
	// Método de teste preliminar.
	public void test() {
		Message message = new Message("Oi DNS Server!");
		udpProvider.send(message, 53, dnsServerIp);
		
		message = new Message("Oi HTTP Server!");
		tcpProvider.send(message, 80, new Ip("192.168.2.2"));
	}
	
	@Override
	public void receive(Message message) {
		System.out.println("Aplicação do client HTTP " + clientName + " recebeu menssagem:");
		System.out.println(message);
		System.out.println("[PROCESSANDO]\n");
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
