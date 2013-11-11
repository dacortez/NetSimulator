package dacortez.netSimulator.application;

import dacortez.netSimulator.application.message.Message;



/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpServer extends Host {
	// Porta em que o processo servidor está escutando.
	public static final Integer LISTEN_PORT = 80;
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
	public void receive(Message message, Socket socket) {
		System.out.println("Aplicação do servidor HTTP " + serverName + " recebeu menssagem:");
		super.receive(message, socket);
	}
	
	@Override
	protected void processReceived(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
