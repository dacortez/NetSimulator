package dacortez.netSimulator.network;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.transport.Segment;
import dacortez.netSimulator.transport.ServiceProvider;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HostInterface extends Interface {
	// Provedor de serviços da camada de transporte associado a esta interface.
	private ServiceProvider serviceProvider;
	
	public HostInterface(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	
	public void send(Segment segment, Ip sourceIp, Ip destinationIp) {
		Datagram data = new Datagram(segment, sourceIp, destinationIp);
		fireNetworkEvent(data);		
	}
	
	@Override
	public void networkEventHandler(Datagram data) {
		System.out.println("Interface do host " + ip + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO DATAGRAMA PARA O PROVEDOR DE SERVIÇOS ADEQUADO]\n");
		serviceProvider.receive(data.getSegment(), data.getSourceIp(), data.getDestinationIp());
	}
}
