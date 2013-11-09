package dacortez.netSimulator.application;

import dacortez.netSimulator.application.process.DnsLooking;
import dacortez.netSimulator.application.process.Process;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HttpClient extends Host {
	// Nome do cliente HTTP.
	private String clientName;
	
	public String getClientName() {
		return clientName;
	}
	
	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
	}
	
	// Método de teste preliminar.
	public void test() {
		Process process = new Process(new DnsLooking());
		process.setDestinationIp(dnsServerIp);
		process.setDestinationPort(DnsServer.LISTEN_PORT);
		processes.add(process);
		serviceProvider.send(process.request(), process);
	}
	
	@Override
	public void receive(Message message, Process process) {
		if (process != null) {
			System.out.println("Aplicação do client HTTP " + clientName + " recebeu menssagem:");
			System.out.println(message);
			System.out.println("[PROCESSANDO]\n");
		}
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
