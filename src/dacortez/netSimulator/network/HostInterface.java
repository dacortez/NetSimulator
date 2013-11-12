package dacortez.netSimulator.network;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.TimeUtil;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.InHost;
import dacortez.netSimulator.events.OutHost;
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
		System.out.println("Interface do host " + ip + " recebeu segmento:");
		System.out.println(segment);
		System.out.println("[ENVIANDO EVENTO OUT_HOST PARA O SIM]\n");
		Datagram data = new Datagram(segment, sourceIp, destinationIp);
		double time = TimeUtil.getEndTime() + link.getTransmissionTime(data.getNumberOfBytes());
		EventArgs args = new EventArgs(data, time);
		OutHost e = new OutHost(this, args);
		fireSimEvent(e);
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		System.out.println("Interface do host " + ip + " recebeu datagrama:");
		System.out.println(args.getDatagram());
		System.out.println("[ENVIANDO EVENTO IN_HOST PARA O SIM]\n");
		InHost e = new InHost(this, args);
		fireSimEvent(e);
	}

	public void receive(EventArgs args) {
		Datagram data = args.getDatagram();
		System.out.println("Interface do host " + ip + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO DATAGRAMA PARA O PROVEDOR DE SERVIÇOS]\n");
		TimeUtil.setStartTime(args.getTime());
		serviceProvider.receive(data.getSegment(), data.getSourceIp(), data.getDestinationIp());
	}	
}
