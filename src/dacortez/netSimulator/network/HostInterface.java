package dacortez.netSimulator.network;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.TimeUtil;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.InterfaceOutcomingData;
import dacortez.netSimulator.events.InterfaceIncomingData;
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
		linkQueue = new ArrayList<EventArgs>();
	}
	
	public void send(Segment segment, Ip sourceIp, Ip destinationIp) {
		Datagram data = new Datagram(segment, sourceIp, destinationIp);
		double time = TimeUtil.getEndTime();
		EventArgs args = new EventArgs(data, time);
		Simulator.addToQueue(new InterfaceOutcomingData(this, args));
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		Simulator.addToQueue(new InterfaceIncomingData(this, args));
	}

	public void receive(EventArgs args) {
		Datagram data = args.getDatagram();
		TimeUtil.setStartTime(args.getTime());
		serviceProvider.receive(data.getSegment(), data.getSourceIp(), data.getDestinationIp());
	}	
}
