package dacortez.netSimulator.application.dns;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class DnsServer extends Host {
	// Porta em que o processo servidor está escutando.
	public static final int LISTEN_PORT = 53;
	// Nome do servidor DNS.
	private String serverName;
	// Mapa de endereços IPs.
	private List<ResourceRecord> resourceRecords;
	// Processo permanente responsável por responder as requisições DNS.
	private Process serverProcess;

	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
		resourceRecords = new ArrayList<ResourceRecord>();
		processes = new ArrayList<Process>();
	}
	
	public void addHost(String name, Ip ip) {
		ResourceRecord rr = new ResourceRecord(name, ip.toString(), RRType.A, 0);
		resourceRecords.add(rr);
		System.out.println("+ Adicionado registro ao servidor dns " + serverName + ": " + rr + "\n");
	}
	
	public void start() {
		Socket socket = new Socket();
		socket.setSourceIp(getIp());
		socket.setSourcePort(LISTEN_PORT);
		serverProcess = new DnsServerProcess(socket, resourceRecords); 
		processes.add(serverProcess);
		System.out.println("# Servidor DNS " + serverName + " escutando na porta " + LISTEN_PORT + ".\n");
	}
	
	@Override
	public void receive(Message message, Process process) {	
		print(serverName, message);
		Message respond = process.respond(message);
		if (respond != null)
			serviceProvider.udpSend(respond, process);
	}
		
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
