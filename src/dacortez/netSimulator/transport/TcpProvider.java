package dacortez.netSimulator.transport;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class TcpProvider extends ServiceProvider {

	public TcpProvider(Interface iface) {
		super(iface);
	}

	@Override
	public void send(Message message, Integer destinationPort, Ip destinationIp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(Datagram datagram) {
		// TODO Auto-generated method stub
		
	}
}
