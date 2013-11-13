package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.RouterIncomingData;
import dacortez.netSimulator.events.DataQueuedInRouter;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.12
 */
public class RouterInterface extends Interface {
	// Roteador associado a esta interface.
	private Router router;
	// O número da porta da inteface do roteador.
	private Integer port;
	// Tamanho da fila em quantidade de pacotes.
	private int queueSize;
	// Fila da interface do roteador.
	private List<EventArgs> queue;
	
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
		linkQueue = new ArrayList<EventArgs>();
		queue = new ArrayList<EventArgs>();
	}
	
	public void queueing(EventArgs args) {
		if (queue.size() < queueSize) {
			Datagram data = args.getDatagram();
			double outOfQueueTime = getOutOfQueueTime(args);
			EventArgs out = new EventArgs(data, outOfQueueTime);
			queue.add(args);
			debugQueue(outOfQueueTime);
			Simulator.addToQueue(new DataQueuedInRouter(this, out));
		}
	}

	private void debugQueue(double outOfQueueTime) {
		if (Simulator.debugMode) {
			System.out.println("***************************************************");
			System.out.println("Router interface " + ip + " QUEUE: " + queue.size());
			System.out.println("Out of queue time: " + outOfQueueTime);
			System.out.println("***************************************************");
			System.out.println();
		}
	}
	
	private double getOutOfQueueTime(EventArgs in) {
		double inTime = in.getTime();
		double processingTime = router.getProcessingTime() / 1000000.0;
		if (queue.isEmpty())
			return inTime + processingTime;
		double first = Double.MAX_VALUE;
		for (EventArgs args: queue)
			if (args.getTime() < first)
				first = args.getTime();
		return first + queue.size() * processingTime;
	}
	
	public void dequeueing(EventArgs args) {
		int index = -1;
		for (EventArgs queueArgs: queue)
			if (queueArgs.getDatagram() == args.getDatagram())
				index = queue.indexOf(queueArgs);
		if (index != -1) {
			queue.remove(index);
			router.route(args.getDatagram(), args.getTime());
		}
	}

	@Override
	public void networkEventHandler(EventArgs args) {
		Simulator.addToQueue(new RouterIncomingData(this, args));
	}
		
	@Override
	public String toString() {
		return "(" + ip + ":" + port + ", fila: " + queueSize + ") => " + link; 
	}
}
