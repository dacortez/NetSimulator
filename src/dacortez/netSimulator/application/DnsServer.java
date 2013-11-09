package dacortez.netSimulator.application;

import dacortez.netSimulator.application.process.DnsListening;
import dacortez.netSimulator.application.process.Process;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class DnsServer extends Host {
	// Porta em que o processo servidor está escutando.
	public static final Integer LISTEN_PORT = 53;
	// Nome do servidor DNS.
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
	}
	
	public void start() {
		Process process = new Process(new DnsListening());
		process.setSourceIp(getIp());
		process.setSourcePort(LISTEN_PORT);
		processes.add(process);
		System.out.println("Servidor DNS " + serverName + " escutando na porta " + LISTEN_PORT);
	}
	
	@Override
	public void receive(Message message, Process process) {
		if (process != null) {
			System.out.println("Aplicação do servidor DNS " + serverName + " recebeu menssagem:");
			System.out.println(message);
			System.out.println("[PROCESSANDO]\n");
			Message response = process.respond(message);
			if (response != null) {
				serviceProvider.send(response, process);
				process.setDestinationIp(null);
				process.setDestinationPort(null);
			}
		}
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
