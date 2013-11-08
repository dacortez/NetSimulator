package dacortez.netSimulator.application;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class DnsServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
	}
	
	@Override
	public void receive(Message message, Process process) {
		if (process != null) {
			System.out.println("Aplicação do servidor DNS " + serverName + " recebeu menssagem:");
			System.out.println(message);
			System.out.println("[PROCESSANDO]\n");
		}
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
