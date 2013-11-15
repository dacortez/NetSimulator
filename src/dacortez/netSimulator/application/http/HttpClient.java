package dacortez.netSimulator.application.http;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;
import dacortez.netSimulator.application.dns.DnsServer;

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
		processes = new ArrayList<Process>();
	}
	
	public void get(String host, String resource) {
		if (!Ip.isValid(host))
			dnsLooking(host, resource);
		else
			httpGetting(host, resource);
	}
	
	private void dnsLooking(String host, String resource) {
		Socket socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		HttpClientProcess process = new HttpClientProcess(socket, host, resource);
		process.setWaitingDns(true);
		processes.add(process);
		serviceProvider.send(process.request(), process);
	}
	
	private void httpGetting(String host, String resource) {
		Socket socket = new Socket();
		socket.setDestinationIp(new Ip(host));
		socket.setDestinationPort(HttpServer.LISTEN_PORT);
		HttpClientProcess process = new HttpClientProcess(socket, host, resource);
		processes.add(process);
		serviceProvider.send(process.request(), process);
	}
	
	@Override
	public void receive(Message message, Process process) {
		System.out.println("Aplicação do client HTTP " + clientName + " recebeu uma mensagem:");
		super.receive(message, process);
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
