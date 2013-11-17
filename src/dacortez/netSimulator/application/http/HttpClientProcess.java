package dacortez.netSimulator.application.http;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;
import dacortez.netSimulator.application.dns.DnsAnswer;
import dacortez.netSimulator.application.dns.DnsMessage;
import dacortez.netSimulator.application.dns.DnsQuestion;
import dacortez.netSimulator.application.dns.RRType;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public class HttpClientProcess extends Process {
	// Host solicitado pelo processo.
	private String host;
	// Recurso solicitado no servidor HTTP.
	private String resource;
	// Sinaliza que o processo está esperando pela resposta do servidor DNS.
	private boolean isWaitingDns;
	// Nome do host rodando o processo cliente HTTP.
	private String clientName;
	// Identificador das mensagens DNS.
	private int dnsId;
	// Contador estático a ser utilizado na identificação das mensagens DNS.
	private static int count = 0;
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public void setWaitingDns(boolean isWaitingDns) {
		this.isWaitingDns = isWaitingDns;
	}
	
	public HttpClientProcess(Socket socket, String host, String resource, String clientName) {
		super(socket);
		this.host = host;
		this.resource = resource;
		this.clientName = clientName;
	}
	
	@Override
	public Process fork() {
		HttpClientProcess process = new HttpClientProcess(socket.clone(), host, resource, clientName);
		process.setWaitingDns(isWaitingDns);
		return process;
	}
	
	@Override
	public Message request() {
		if (isWaitingDns) return dnsMessage();
		return httpRequest();
	}
	
	private DnsMessage dnsMessage() {
		dnsId = ++count;
		DnsMessage message = new DnsMessage(dnsId, false);
		message.addQuestion(new DnsQuestion(host, RRType.A));
		return message;
	}
	
	private HttpRequest httpRequest() {
		String resource = (this.resource == null) ? "index.html" : this.resource;
		HttpRequest request = new HttpRequest(HttpMethod.GET, resource);
		request.setHost(host);
		request.setUserAgent(clientName);
		request.addHttpAccept(HttpAccept.HTML);
		request.addHttpAccept(HttpAccept.PNG);
		return request;
	}
	
	@Override
	public Message respond(Message message) {
		if (isWaitingDns) {
			DnsMessage reply = (DnsMessage) message;
			if (reply.getId() == dnsId)
				return httpRequest(reply);
		}
		return null;
	}

	private Message httpRequest(DnsMessage reply) {
		Ip hostIp = getIp(reply);
		if (hostIp != null) {
			socket.setDestinationIp(hostIp);
			socket.setDestinationPort(HttpServer.LISTEN_PORT);
			isWaitingDns = false;
			return httpRequest();
		}
		return null;
	}
	
	private Ip getIp(DnsMessage reply) {
		if (reply.isReply() && !reply.getAnswears().isEmpty()) {
			DnsAnswer answer = reply.getAnswears().get(0);
			return new Ip(answer.getValue());
		}
		return null;
	}
}
