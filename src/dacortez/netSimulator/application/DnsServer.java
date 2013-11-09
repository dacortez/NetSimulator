package dacortez.netSimulator.application;

import java.util.HashMap;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.process.DnsServerListening;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class DnsServer extends Host {
	// Porta em que o processo servidor está escutando.
	public static final Integer LISTEN_PORT = 53;
	// Nome do servidor DNS.
	private String serverName;
	// Mapa de endereços IPs.
	private HashMap<String, Ip> ipsMap;

	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
		ipsMap = new HashMap<String, Ip>();
	}
	
	public void addHost(String name, Ip ip) {
		ipsMap.put(name, ip);
	}
	
	public Ip getIpForHost(String name) {
		return ipsMap.get(name);
	}
	
	public void start() {
		socket = new Socket();
		socket.setSourceIp(getIp());
		socket.setSourcePort(LISTEN_PORT);
		state = new DnsServerListening();
		System.out.println("Servidor DNS " + serverName + " escutando na porta " + LISTEN_PORT + "\n");
	}
	
	@Override
	public void receive(Message message, Socket socket) {
		if (socket != null) {
			System.out.println("Aplicação do servidor DNS " + serverName + " recebeu menssagem:");
			System.out.println(message);
			System.out.println("[PROCESSANDO]\n");
			Message response = state.respond(message);
			if (response != null) {
				serviceProvider.send(response, socket);
				socket.setDestinationIp(null);
				socket.setDestinationPort(null);
			}
		}
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
