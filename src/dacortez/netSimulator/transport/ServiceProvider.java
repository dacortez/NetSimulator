package dacortez.netSimulator.transport;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.HostInterface;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class ServiceProvider {	
	// Host associado a este provedor de serviços da camada de transporte.
	private Host host;
	// Interface do host associado.
	private HostInterface hostInterface;
	
	public void setHost(Host host) {
		this.host = host;
	}
	
	public HostInterface getHostInterface() {
		return hostInterface;
	}
	
	public ServiceProvider() {
		hostInterface = new HostInterface(this);
	}
	
	public void send(Message message, Integer destinationPort, Ip destinationIp) {
		Segment segment = new Segment(message, 1000, destinationPort);
		Ip sourceIp = hostInterface.getIp();
		Datagram data = new Datagram(segment, sourceIp, destinationIp);
		hostInterface.fireNetworkEvent(data);
	}

	public void receive(Datagram data) {
		System.out.println("Provider de serviços do host " + hostInterface.getIp() + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO MENSSAGEM PARA A APLICAÇÃO]\n");
		host.receive(data.getSegment().getMessage());	
	}
	
	@Override
	public String toString() {
		return hostInterface.toString();
	}
}
