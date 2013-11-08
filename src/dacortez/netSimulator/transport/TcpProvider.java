package dacortez.netSimulator.transport;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class TcpProvider extends ServiceProvider {
	
	public TcpProvider(Host host) {
		super(host);
	}
	
	@Override
	public void send(Message message, Integer destinationPort, Ip destinationIp) {
		Segment segment = new Segment(message, 1000, destinationPort);
		Ip sourceIp = host.getHostInterface().getIp();
		Datagram data = new Datagram(segment, sourceIp, destinationIp);
		host.getHostInterface().fireNetworkEvent(data);
	}

	@Override
	public void receive(Datagram data) {
		System.out.println("TcpProvider do host " + host.getHostInterface().getIp() + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO MENSSAGEM PARA A APLICAÇÃO]\n");
		host.receive(data.getSegment().getMessage());
	}
}
