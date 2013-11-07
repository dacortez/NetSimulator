package dacortez.netSimulator.transport;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public abstract class ServiceProvider {
	// Interface do host associado a este provedor de serviços.
	protected Interface iface;
	
	public ServiceProvider(Interface iface) {
		this.iface = iface;
	}
	
	public abstract void send(Message message, Integer destinationPort, Ip destinationIp);
	
	public abstract void receive(Datagram datagram);
}
