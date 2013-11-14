package dacortez.netSimulator.application;

import java.util.ArrayList;
import java.util.HashMap;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.messages.Message;

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
	private HashMap<String, Ip> ipsMap;
	// Processo permanente responsável por ficar escutando as requisições.
	private Process serverProcess;

	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
		ipsMap = new HashMap<String, Ip>();
		processes = new ArrayList<Process>();
	}
	
	public void addHost(String name, Ip ip) {
		ipsMap.put(name, ip);
	}
	
	public void start() {
		Socket socket = new Socket();
		socket.setSourceIp(getIp());
		socket.setSourcePort(LISTEN_PORT);
		serverProcess = new Process(socket, ProcessState.DNS_LISTENING); 
		processes.add(serverProcess);
		System.out.println("# Servidor DNS " + serverName + " escutando na porta " + LISTEN_PORT + ".\n");
	}
	
	@Override
	public void receive(Message message, Process process) {
		System.out.println("Aplicação do servidor DNS " + serverName + " recebeu menssagem:");
		super.receive(message, process);
	}
	
	@Override
	protected void handleReceived(Message message, ProcessState state) {
		if (state == ProcessState.DNS_LISTENING) {
			String name = message.getData();
			Ip ip = ipsMap.get(name);
			Message response;
			if (ip != null)
				response = new Message(ip.toString());
			else 
				response = new Message("NAO");
			serviceProvider.send(response, serverProcess);
			serverProcess.getSocket().setDestinationIp(null);
			serverProcess.getSocket().setDestinationPort(null);
		}
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
