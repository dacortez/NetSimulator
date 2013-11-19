package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.20
 */
public class Timeout extends SimEvent {

	public Timeout(Interface sender, EventArgs args) {
		super(sender, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fire() {
		//Chronometer.setTime(args.getTime());
		//HostInterface hi = (HostInterface) sender;
		//ServiceProvider sp = hi.getServiceProvider();
		//Datagram data = args.getDatagram();
		//TcpSegment segment = (TcpSegment) data.getSegment();
		//Ip sourceIp = data.getSourceIp();
		//Ip destinationIp = data.getDestinationIp();
		//sp.timeout(segment, sourceIp, destinationIp);
	}
	
	@Override
	public String toString() {
		return "TIMEOUT:\n" + super.toString();
	}
}
