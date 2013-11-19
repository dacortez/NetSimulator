package dacortez.netSimulator.application.http;

import java.util.ArrayList;

import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

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
		serverProcess = new HttpServerProcess(socket, serverName); 
		processes.add(serverProcess);
		System.out.println("# Servidor HTTP " + serverName + " escutando na porta " + LISTEN_PORT + ".\n");
	}
	
	@Override
	public void receive(Message message, Process process) {	
		print(serverName, message);
		Message respond = process.respond(message);
			if (respond != null)
				serviceProvider.tcpSend(respond, process);
	}
		
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
