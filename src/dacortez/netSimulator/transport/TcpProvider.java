package dacortez.netSimulator.transport;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class TcpProvider extends ServiceProvider {
	
	public TcpProvider(HostInterface hostInterface) {
		super(hostInterface);
	}
	
	@Override
	public void send(Message message, Integer destinationPort, Ip destinationIp) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void transportEventHandler(Segment segment) {
		System.out.println("TcpProvide recebeu segmento:");
		System.out.println(segment);
		System.out.println();
		super.transportEventHandler(segment);
	}
}
