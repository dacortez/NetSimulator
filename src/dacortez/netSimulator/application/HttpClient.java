package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;


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
		Message message = new Message("Oi DNS Server!");
		serviceProvider.send(message, 53, dnsServerIp);
		
		message = new Message("Oi HTTP Server!");
		serviceProvider.send(message, 80, new Ip("192.168.2.2"));
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
