package dacortez.netSimulator.events;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.TimeUtil;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.network.Interface;
import dacortez.netSimulator.transport.ServiceProvider;
import dacortez.netSimulator.transport.TcpSegment;

public class Timeout extends SimEvent {

	public Timeout(Interface sender, EventArgs args) {
		super(sender, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fire() {
		TimeUtil.setStartTime(args.getTime());
		HostInterface hi = (HostInterface) sender;
		ServiceProvider sp = hi.getServiceProvider();
		Datagram data = args.getDatagram();
		TcpSegment segment = (TcpSegment) data.getSegment();
		Ip sourceIp = data.getSourceIp();
		Ip destinationIp = data.getDestinationIp();
		sp.timeout(segment, sourceIp, destinationIp);
	}
	
	@Override
	public String toString() {
		return "TIMEOUT:\n" + super.toString();
	}
}
