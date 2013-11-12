package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.messages.DnsQuery;
import dacortez.netSimulator.application.messages.HttpRequest;
import dacortez.netSimulator.application.messages.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpClient extends Host {
	// Nome do cliente HTTP.
	private String clientName;
	
	// Talvez criar uma lista de processos contendo essas informações e vincular com o socket criado.
	// Último host solicitada.
	private String host;
	// Último recurso solicitado
	private String resource;
	
	public String getClientName() {
		return clientName;
	}
	
	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
	}
	
	public void get(String host, String resource) {
		this.host = host;
		this.resource = resource;
		if (Ip.isValid(host))
			httpGetting(new Ip(host));
		else
			dnsLooking(host);
	}
	
	private void httpGetting(Ip host) {
		socket = new Socket();
		socket.setDestinationIp(host);
		socket.setDestinationPort(HttpServer.LISTEN_PORT);
		state = AppState.HTTP_GETTING;
		serviceProvider.send(httpGetMessage(), socket);
	}
	
	private HttpRequest httpGetMessage() {
		if (resource != null)
			return new HttpRequest("GET " + host + " /" + resource);
		return new HttpRequest("GET " + host);
	}

	private void dnsLooking(String host) {
		socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		state = AppState.DNS_LOOKING;
		serviceProvider.send(dnsQueryMessage(host), socket);
	}
	
	private DnsQuery dnsQueryMessage(String host) {
		return new DnsQuery(host);
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
