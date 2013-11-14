package dacortez.netSimulator.application.dns;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.ProcessState;
import dacortez.netSimulator.application.Socket;
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
	private List<ResourceRecord> resourceRecords;
	// Processo permanente responsável por ficar escutando as requisições.
	private Process serverProcess;

	public String getServerName() {
		return serverName;
	}

	public DnsServer(String serverName) {
		super();
		this.serverName = serverName;
		resourceRecords = new ArrayList<ResourceRecord>();
		processes = new ArrayList<Process>();
	}
	
	public void addHost(String name, Ip ip) {
		ResourceRecord rr = new ResourceRecord(name, ip.toString(), RRType.A, 0);
		resourceRecords.add(rr);
		System.out.println("+ Adicionado registro ao servidor dns " + serverName + ": " + rr + "\n");
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
			if (message instanceof DnsMessage)
				sendAnswers((DnsMessage) message);
			serverProcess.getSocket().setDestinationIp(null);
			serverProcess.getSocket().setDestinationPort(null);
		}
	}

	private void sendAnswers(DnsMessage request) {
		if (!request.isReply()) {
			DnsMessage message = new DnsMessage(request.getId(), true);
			for (DnsQuestion question: request.getQuestions()) {
				DnsAnswer answer = answer(question);
				if (answer != null) message.addAnswer(answer);
			}
			serviceProvider.send(message, serverProcess);
		}
	}
	
	private DnsAnswer answer(DnsQuestion question) {
		for (ResourceRecord rr: resourceRecords)
			if (question.getName().contentEquals(rr.getName())) {
				RRType type = rr.getType();
				String value = rr.getValue();
				Integer ttl = rr.getTtl();
				return new DnsAnswer(type, value, ttl); 
			}
		return null;
	}
	
	@Override
	public String toString() {
		return serverName + " <- " + super.toString();
	}
}
