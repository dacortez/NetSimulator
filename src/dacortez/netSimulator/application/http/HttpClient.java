package dacortez.netSimulator.application.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
		try {
			ps = new PrintStream(new File("/tmp/" + clientName));
		} catch (FileNotFoundException e) {
			System.err.println("Erro ao criar arquivo dos dados recebidos no host " + clientName + ".");
			System.err.println("(os dados serão mostrados apenas na saída padrão).");
			ps = null;
		}
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
	
	@Override
	public void receive(Message message, Process process) {
		print(message);
		if (process != null) {
			Message respond = process.respond(message);
			if (respond != null)
				serviceProvider.tcpSend(respond, process);
		}
		else 
			System.out.println("Socket fechado!\n");
	}

	private void print(Message message) {
		StringBuilder sb = new StringBuilder();
		sb.append("Aplicação do cliente HTTP " + clientName + " recebeu uma mensagem:\n");
		sb.append(message).append("\n");
		System.out.println(sb.toString());
		if (ps != null) ps.println(sb.toString());
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
