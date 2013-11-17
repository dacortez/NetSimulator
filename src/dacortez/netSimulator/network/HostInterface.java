package dacortez.netSimulator.network;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.TimeUtil;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.OutboundData;
import dacortez.netSimulator.events.InboundData;
import dacortez.netSimulator.events.Timeout;
import dacortez.netSimulator.transport.Protocol;
import dacortez.netSimulator.transport.Segment;
import dacortez.netSimulator.transport.ServiceProvider;
import dacortez.netSimulator.transport.TcpController;
import dacortez.netSimulator.transport.TcpSegment;
import dacortez.netSimulator.transport.UdpSegment;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class HostInterface extends Interface {
	// Provedor de serviços da camada de transporte associado a esta interface.
	private ServiceProvider serviceProvider;
	
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	
	public HostInterface(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
		queue = new ArrayList<EventArgs>();
	}
	
	public void send(Segment segment, Ip sourceIp, Ip destinationIp) {
		Datagram data = new Datagram(segment, sourceIp, destinationIp);
		double time = TimeUtil.getEndTime();
		EventArgs out = new EventArgs(data, time);
		Simulator.addToQueue(new OutboundData(this, out));
		if (data.getUperLayerProtocol() == Protocol.TCP) {
			EventArgs timeout = new EventArgs(data, time + TcpController.TIMEOUT / 1000.0);
			Simulator.addToQueue(new Timeout(this, timeout));
		}
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		Simulator.addToQueue(new InboundData(this, args));
	}

	public void receive(EventArgs args) {
		TimeUtil.setStartTime(args.getTime());
		Datagram data = args.getDatagram();
		if (data.getUperLayerProtocol() == Protocol.UDP) {
			UdpSegment segment = (UdpSegment) data.getSegment();
			serviceProvider.udpReceive(segment, data.getSourceIp(), data.getDestinationIp());
		}
		else if (data.getUperLayerProtocol() == Protocol.TCP) {
			TcpSegment segment = (TcpSegment) data.getSegment();
			serviceProvider.tcpReceive(segment, data.getSourceIp(), data.getDestinationIp());
		}
	}	
}
