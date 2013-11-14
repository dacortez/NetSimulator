package dacortez.netSimulator.application;

import java.util.ArrayList;

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
	// Processo permanente responsável por ficar escutando as requisições.
	private Process serverProcess;
	
	public String getServerName() {
		return serverName;
	}

	public HttpServer(String serverName) {
		super();
		this.serverName = serverName;
		processes = new ArrayList<Process>();
	}
	
	public void start() {
		Socket socket = new Socket();
		socket.setSourceIp(getIp());
		socket.setSourcePort(LISTEN_PORT);
		serverProcess = new Process(socket, ProcessState.HTTP_LISTENING); 
		processes.add(serverProcess);
		System.out.println("# Servidor HTTP " + serverName + " escutando na porta " + LISTEN_PORT + ".\n");
	}
	
	@Override
	public void receive(Message message, Process process) {
		System.out.println("Aplicação do servidor HTTP " + serverName + " recebeu menssagem:");
		super.receive(message, process);
	}
	
	@Override
	protected void handleReceived(Message message, ProcessState state) {
		Message response = new Message("HTTP/1.1 200 OK");
		serviceProvider.send(response, serverProcess);
		serverProcess.getSocket().setDestinationIp(null);
		serverProcess.getSocket().setDestinationPort(null);
	}

	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
