package dacortez.netSimulator.transport;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.network.Datagram;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public abstract class ServiceProvider {	
	// Host associado a este provedor de serviços da camada de transporte.
	protected Host host;
	
	public ServiceProvider(Host host) {
		this.host = host;
	}

	public abstract void send(Message message, Integer destinationPort, Ip destinationIp);
	
	public abstract void receive(Datagram data);
}
