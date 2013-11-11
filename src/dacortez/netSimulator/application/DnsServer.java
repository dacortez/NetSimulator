package dacortez.netSimulator.application;

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
	
	public void start() {
		socket = new Socket();
		socket.setSourceIp(getIp());
		socket.setSourcePort(LISTEN_PORT);
		state = AppState.DNS_LISTENING;
		System.out.println("Servidor DNS " + serverName + " escutando na porta " + LISTEN_PORT + "\n");
	}
	
	@Override
	public void receive(Message message, Socket socket) {
		System.out.println("Aplicação do servidor DNS " + serverName + " recebeu menssagem:");
		super.receive(message, socket);
	}
	
	@Override
	protected void processReceived(Message message) {
		if (state == AppState.DNS_LISTENING) {
			String name = message.getData();
			Ip ip = ipsMap.get(name);
			Message response;
			if (ip != null)
				response = new Message(ip.toString());
			else 
				response = new Message("NAO");
			serviceProvider.send(response, socket);
			socket.setDestinationIp(null);
			socket.setDestinationPort(null);
		}
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
