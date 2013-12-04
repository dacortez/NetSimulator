package dacortez.netSimulator.application.http;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;
import dacortez.netSimulator.application.TracerouteProcess;
import dacortez.netSimulator.application.dns.DnsServer;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.12.03
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
			httpDnsLooking(host, resource);
		else
			httpGetting(host, resource);
	}
	
	private void httpDnsLooking(String host, String resource) {
		Socket socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		HttpClientProcess process = new HttpClientProcess(socket, host, resource, clientName);
		process.setWaitingDns(true);
		processes.add(process);
		serviceProvider.udpSend(process.request(), process);
	}
	
	private void httpGetting(String host, String resource) {
		Socket socket = new Socket();
		socket.setDestinationIp(new Ip(host));
		socket.setDestinationPort(HttpServer.LISTEN_PORT);
		HttpClientProcess process = new HttpClientProcess(socket, host, resource, clientName);
		processes.add(process);
		serviceProvider.tcpSend(process.request(), process);
	}
	
	public void traceroute(String host) {
		if (!Ip.isValid(host))
			tracerouteDnsLooking(host);
		else
			tracerouting(host);
	}
	
	private void tracerouteDnsLooking(String host) {
		Socket socket = new Socket();
		socket.setDestinationIp(dnsServerIp);
		socket.setDestinationPort(DnsServer.LISTEN_PORT);
		TracerouteProcess process = new TracerouteProcess(socket, host, clientName);
		process.setHostInterface(serviceProvider.getHostInterface());
		processes.add(process);
		serviceProvider.udpSend(process.request(), process);
	}
	
	private void tracerouting(String host) {
		Socket socket = new Socket();
		socket.setDestinationIp(new Ip(host));
		TracerouteProcess process = new TracerouteProcess(socket, clientName);
		process.setHostInterface(serviceProvider.getHostInterface());
		serviceProvider.bindProcessSocket(process);
		processes.add(process);
		process.sendProbes();
	}

	@Override
	public void receive(Message message, Process process) {	
		print(clientName, message);
		if (process instanceof HttpClientProcess) {
			Message respond = process.respond(message);
			if (respond != null) 
				serviceProvider.tcpSend(respond, process);
		}
		else if (process instanceof TracerouteProcess) {
			process.respond(message);
			TracerouteProcess trp = (TracerouteProcess) process;
			trp.sendProbes();
		}
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
