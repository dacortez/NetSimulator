package dacortez.netSimulator.application;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.dns.DnsAnswer;
import dacortez.netSimulator.application.dns.DnsMessage;
import dacortez.netSimulator.application.dns.DnsQuestion;
import dacortez.netSimulator.application.dns.DnsServer;
import dacortez.netSimulator.application.dns.RRType;
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
		serviceProvider.send(dnsMessage(host), process);
	}
	
	private DnsMessage dnsMessage(String host) {
		DnsMessage message = new DnsMessage(0, false);
		message.addQuestion(new DnsQuestion(host, RRType.A));
		return message;
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
			if (message instanceof DnsMessage) {
				Ip hostIp = getIp((DnsMessage) message);
				if (hostIp != null) 
					httpGetting(hostIp);
			}
		}
		else if (state == ProcessState.HTTP_GETTING) {

		}
	}

	private Ip getIp(DnsMessage reply) {
		if (reply.isReply() && !reply.getAnswears().isEmpty()) {
			DnsAnswer answer = reply.getAnswears().get(0);
			return new Ip(answer.getValue());
		}
		return null;
	}

	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
