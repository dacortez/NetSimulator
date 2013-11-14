package dacortez.netSimulator.application;

import java.util.ArrayList;

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
	// Último recurso solicitado
	private String resource;
	
	public String getClientName() {
		return clientName;
	}
	
	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
		processes = new ArrayList<Process>();
	}
	
	public void get(String host, String resource) {
		this.resource = resource;
		if (!Ip.isValid(host))
			dnsLooking(host);
		else
			httpGetting(new Ip(host));
	}
	
	private void dnsLooking(String host) {
		Socket socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		Process process = new Process(socket, ProcessState.DNS_LOOKING);
		processes.add(process);
		serviceProvider.send(dnsQueryMessage(host), process);
	}
	
	private DnsQuery dnsQueryMessage(String host) {
		return new DnsQuery(host);
	}
	
	private void httpGetting(Ip host) {
		Socket socket = new Socket();
		socket.setDestinationIp(host);
		socket.setDestinationPort(HttpServer.LISTEN_PORT);
		Process process = new Process(socket, ProcessState.HTTP_GETTING);
		processes.add(process);
		serviceProvider.send(httpGetMessage(), process);
	}
	
	private HttpRequest httpGetMessage() {
		if (resource != null)
			return new HttpRequest("GET /" + resource);
		return new HttpRequest("GET /");
	}
	
	@Override
	public void receive(Message message, Process process) {
		System.out.println("Aplicação do client HTTP " + clientName + " recebeu menssagem:");
		super.receive(message, process);
		if (process != null) 
			processes.remove(process);
	}
	
	@Override
	protected void handleReceived(Message message, ProcessState state) {
		if (state == ProcessState.DNS_LOOKING) {
			if (!message.getData().contentEquals("NAO")) {
				httpGetting(new Ip(message.getData()));
			}
		}
		else if (state == ProcessState.HTTP_GETTING) {

		}
	}

	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
