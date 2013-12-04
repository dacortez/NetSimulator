package dacortez.netSimulator.network;

import java.util.ArrayList;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.transport.IcmpSegment;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.12.01
 */
public class RouterInterface extends Interface {
	// Roteador associado a esta interface.
	private Router router;
	// O número da porta da inteface do roteador.
	private Integer port;
	// Tamanho da fila em quantidade de pacotes.
	private int queueSize;
	
	public Integer getPort() {
		return port;
	}
	
	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
			
	public RouterInterface(Router router, Integer port) {
		super();
		this.router = router;
		this.port = port;
		queue = new ArrayList<EventArgs>();
	}
	
	@Override
	public void queuing(EventArgs args) {
		if (queue.size() < queueSize)
			super.queuing(args);
		else {
			if (Simulator.debugMode) {
				System.out.println("--- Pacote perdido em " + ip + ":" + port + ":\n");
				System.out.println("DATAGRAM #" + args.getDatagram().getId());
			}
		}
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		Datagram data = args.getDatagram();
		double time = args.getTime();
		if (checkTtl(data))
			router.route(data, time);
		else {
			Datagram expired = ttlExpired(data);
			router.route(expired, time);
		}
	}

	private Datagram ttlExpired(Datagram data) {
		Ip sourceIp = ip;
		Integer sourcePort = data.getSegment().getDestinationPort();
		Ip destinationIp = data.getSourceIp();
		Integer destinationPort = data.getSegment().getSourcePort();
		IcmpSegment icmpSegment = new IcmpSegment(sourcePort, destinationPort);
		icmpSegment.setType(11);
		icmpSegment.setCode(0);
		icmpSegment.setDescription("TTL expired");
		return new Datagram(icmpSegment, sourceIp, destinationIp);
	}
	
	private boolean checkTtl(Datagram data) {
		if (data.decrementTtl() == 0) {
			if (Simulator.debugMode) {
				System.out.println("--- TTL = 0 (pacote perdido) na interface " + ip + ":\n");
				System.out.println("DATAGRAM #" + data.getId());
			}
			return false;
		}
		return true;
	}
		
	@Override
	public String toString() {
		return "(" + ip + ":" + port + ", queue_size: " + queueSize + ") => " + link; 
	}
}
