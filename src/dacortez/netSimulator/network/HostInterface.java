package dacortez.netSimulator.network;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.Chronometer;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.OutboundData;
import dacortez.netSimulator.events.InboundData;
import dacortez.netSimulator.events.Timeout;
import dacortez.netSimulator.transport.IcmpSegment;
import dacortez.netSimulator.transport.Protocol;
import dacortez.netSimulator.transport.Segment;
import dacortez.netSimulator.transport.ServiceProvider;
import dacortez.netSimulator.transport.TcpController;
import dacortez.netSimulator.transport.TcpSegment;
import dacortez.netSimulator.transport.UdpSegment;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.18
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
		double time = Chronometer.getTime();
		EventArgs out = new EventArgs(data, time);
		Simulator.addToQueue(new OutboundData(this, out));
		addTimeoutEventIfNotAckSynFin(segment, out);
	}

	private void addTimeoutEventIfNotAckSynFin(Segment segment, EventArgs out) {
		Datagram data = out.getDatagram();
		if (data.getUperLayerProtocol() == Protocol.TCP) {
			TcpSegment tcp = (TcpSegment) segment;
			if (!tcp.isAck() && !tcp.isSyn() && !tcp.isFin()) {
				double time = out.getTime() + TcpController.TIMEOUT / 1000.0;
				EventArgs timeout = new EventArgs(data, time);
				Simulator.addToQueue(new Timeout(this, timeout));
			}
		}
	}
	
	public void send(Datagram data, double time) {
		EventArgs out = new EventArgs(data, time);
		Simulator.addToQueue(new OutboundData(this, out));
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		Simulator.addToQueue(new InboundData(this, args));
	}

	public void receive(EventArgs args) {
		Chronometer.setTime(args.getTime());
		Datagram data = args.getDatagram();
		if (data.getUperLayerProtocol() == Protocol.UDP) {
			UdpSegment segment = (UdpSegment) data.getSegment();
			serviceProvider.udpReceive(segment, data.getSourceIp(), data.getDestinationIp());
		}
		else if (data.getUperLayerProtocol() == Protocol.TCP) {
			TcpSegment segment = (TcpSegment) data.getSegment();
			serviceProvider.tcpReceive(segment, data.getSourceIp(), data.getDestinationIp());
		}
		else if (data.getUperLayerProtocol() == Protocol.ICMP) {
			IcmpSegment segment = (IcmpSegment) data.getSegment();
			serviceProvider.icmpReceive(segment, data.getSourceIp(), data.getDestinationIp());
		}
	}	
}
