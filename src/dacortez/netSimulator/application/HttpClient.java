package dacortez.netSimulator.application;

import dacortez.netSimulator.application.process.DnsLooking;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpClient extends Host {
	// Nome do cliente HTTP.
	private String clientName;
	
	public String getClientName() {
		return clientName;
	}
	
	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
	}
	
	// Método de teste preliminar.
	public void test() {
		socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		state = new DnsLooking();
		serviceProvider.send(state.request(), socket);
	}
	
	@Override
	public void receive(Message message, Socket socket) {
		if (socket != null) {
			System.out.println("Aplicação do client HTTP " + clientName + " recebeu menssagem:");
			System.out.println(message);
			System.out.println("[PROCESSANDO]\n");
		}
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
