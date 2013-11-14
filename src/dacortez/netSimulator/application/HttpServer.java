package dacortez.netSimulator.application;

import dacortez.netSimulator.application.messages.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class HttpServer extends Host {
	// Porta em que o processo servidor está escutando.
	public static final int LISTEN_PORT = 80;
	// Nome do servidor HTTP.
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}

	public HttpServer(String serverName) {
		super();
		this.serverName = serverName;
	}
	
	public void start() {
		socket = new Socket();
		socket.setSourceIp(getIp());
		socket.setSourcePort(LISTEN_PORT);
		state = AppState.HTTP_LISTENING;
		System.out.println("# Servidor HTTP " + serverName + " escutando na porta " + LISTEN_PORT + ".\n");
	}
	
	@Override
	public void receive(Message message, Socket socket) {
		System.out.println("Aplicação do servidor HTTP " + serverName + " recebeu menssagem:");
		super.receive(message, socket);
	}
	
	@Override
	protected void processReceived(Message message) {
		Message response = new Message("HTTP/1.1 200 OK");
		serviceProvider.send(response, socket);
		socket.setDestinationIp(null);
		socket.setDestinationPort(null);
	}

	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
