package dacortez.netSimulator.transport;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class UdpProvider extends ServiceProvider {

	public UdpProvider(HostInterface hostInterface) {
		super(hostInterface);
	}
	
	@Override
	public void send(Message message, Integer destinationPort, Ip destinationIp) {
		Segment segment = new Segment(message, 1000, destinationPort);
		Datagram data = new Datagram(segment, hostInterface.getIp(), destinationIp);
		hostInterface.send(data);
	}
	
	@Override
	public void transportEventHandler(Segment segment) {
		System.out.println("UdpProvide recebeu segmento:");
		System.out.println(segment);
		System.out.println();
		super.transportEventHandler(segment);
	}
}
