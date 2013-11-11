package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.messages.Message;

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
	
	public void get(String host) {
		if (Ip.isAddress(host))
			httpGetting(new Ip(host));
		else
			dnsLooking(host);
	}
	
	private void httpGetting(Ip host) {
		socket = new Socket();
		socket.setDestinationIp(host);
		socket.setDestinationPort(HttpServer.LISTEN_PORT);
		state = AppState.HTTP_GETTING;
		serviceProvider.send(httpGettingMessage(), socket);
	}
	
	private Message httpGettingMessage() {
		// TODO Auto-generated method stub
		return new Message("GET /");
	}

	private void dnsLooking(String host) {
		socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		state = AppState.DNS_LOOKING;
		serviceProvider.send(dnsLookingMessage(host), socket);
	}
	
	private Message dnsLookingMessage(String host) {
		return new Message(host);
	}
	
	@Override
	public void receive(Message message, Socket socket) {
		System.out.println("Aplicação do client HTTP " + clientName + " recebeu menssagem:");
		super.receive(message, socket);
	}
	
	@Override
	protected void processReceived(Message message) {
		if (state == AppState.DNS_LOOKING) {
			if (!message.getData().contentEquals("NAO")) {
				httpGetting(new Ip(message.getData()));
			}
		}
		else if (state == AppState.HTTP_GETTING) {

		}
	}

	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
