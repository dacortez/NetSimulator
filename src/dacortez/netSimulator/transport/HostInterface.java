package dacortez.netSimulator.transport;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.application.DnsServer;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.HttpClient;
import dacortez.netSimulator.application.HttpServer;
import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HostInterface extends Interface {
	// Host associado a esta interface.
	private Host host;
	
	public void setHost(Host host) {
		this.host = host;
	}
	
	@Override
	public void networkEventHandler(Datagram data) {
		System.out.println("Interface do host " + ip + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO DATAGRAMA PARA O PROVEDOR DE SERVIÇOS ADEQUADO]\n");
		if (host instanceof HttpServer) {
			HttpServer server = (HttpServer) host;
			server.getTcpProvider().receive(data);
		}
		else if (host instanceof HttpClient) {
			HttpClient client = (HttpClient) host;
			client.getTcpProvider().receive(data);
			client.getUdpProvider().receive(data);
		}
		else if (host instanceof DnsServer) {
			DnsServer server = (DnsServer) host;
			server.getUdpProvider().receive(data);
		}
	}
}
