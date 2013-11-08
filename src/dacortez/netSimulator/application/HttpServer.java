package dacortez.netSimulator.application;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpServer extends Host {
	// Nome do servidor DNS.
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}

	public HttpServer(String serverName) {
		super();
		this.serverName = serverName;
	}
	
	@Override
	public void receive(Message message) {
		System.out.println("Aplicação do servidor HTTP " + serverName + " recebeu menssagem:");
		System.out.println(message);
		System.out.println("[PROCESSANDO]\n");
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
