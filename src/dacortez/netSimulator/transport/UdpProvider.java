package dacortez.netSimulator.transport;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class UdpProvider extends ServiceProvider {

	public UdpProvider(Interface iface) {
		super(iface);
	}

	@Override
	public void send(Message message, Integer destinationPort, Ip destinationIp) {
		Segment segment = new Segment(message, 1000, destinationPort);
		Datagram data = new Datagram(segment, iface.getIp(), destinationIp);
		iface.send(data);
	}

	@Override
	public void receive(Datagram datagram) {
		// TODO Auto-generated method stub
		
	}
}
