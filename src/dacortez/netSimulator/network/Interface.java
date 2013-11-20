package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.QueuedData;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public abstract class Interface implements NetworkEventListener {
	// Endereço IP da interface.
	protected Ip ip;
	// Enlace ao qual a interface está conectada.
	protected DuplexLink link;
	// Fila de datagrams a serem enviados pelo link.
	protected List<EventArgs> queue;
	// Coleção de classes registradas ao evento NetworkEvent (sniffers e outras interfaces).
	private List<NetworkEventListener> networkEventListeners;
	
	public Ip getIp() {
		return ip;
	}

	public void setIp(Ip ip) {
		this.ip = ip;
	}
		
	public DuplexLink getLink() {
		return link;
	}

	public void setLink(DuplexLink link) {
		this.link = link;
	}
	
	public void addNetworkEventListener(NetworkEventListener listener) {
		if (networkEventListeners == null)
			networkEventListeners = new ArrayList<NetworkEventListener>();
		networkEventListeners.add(listener);
	}
	
	public void fireNetworkEvent(EventArgs args) {
		if (networkEventListeners != null)
			for (NetworkEventListener listener: networkEventListeners)
				listener.networkEventHandler(args);
	}
	
	public void queuing(EventArgs args) {
		Datagram data = args.getDatagram();
		double timeAtDestination = getTimeAtDestination(args);
		EventArgs destination = new EventArgs(data, timeAtDestination);
		queue.add(destination);
		queueStatus("queuing", timeAtDestination);
		Simulator.addToQueue(new QueuedData(this, destination));
	}
	
	private double getTimeAtDestination(EventArgs in) {
		Datagram data = in.getDatagram();
		double inTime = in.getTime();
		double delayToSend = delayToSend(data.getNumberOfBytes());
		if (queue.isEmpty())
			return inTime + delayToSend;
		return maxOnTheQueue() + delayToSend;
	}
	
	private double maxOnTheQueue() {
		double max = 0.0;
		for (EventArgs args: queue)
			if (args.getTime() > max)
				max = args.getTime();
		return max;
	}
	
	private double delayToSend(int numberOfBytes) {
		double transmissionDelay = link.getTransmissionDelay(numberOfBytes);
		double propagationDelay = link.getPropagationDelay();
		return transmissionDelay + propagationDelay;
	}

	private void queueStatus(String operation, double timeAtDestination) {
		if (Simulator.debugMode) {
			System.out.println("********************************************************************");
			System.out.println(ip + " QUEUE: " + queue.size());
			System.out.println("Time at destination of queued/dequeued element: " + timeAtDestination);
			System.out.println(operation);
			for (EventArgs args: queue)
				System.out.println(args.getTime());
			System.out.println("********************************************************************\n");
		}
	}
	
	public void dequeuing(EventArgs args) {
		int index = -1;
		for (EventArgs queueArgs: queue)
			if (queueArgs.getDatagram() == args.getDatagram())
				index = queue.indexOf(queueArgs);
		if (index != -1) {
			queue.remove(index);
			queueStatus("dequeuing", args.getTime());
			fireNetworkEvent(args);
		}
	}
	
	@Override
	public abstract void networkEventHandler(EventArgs args);
	
	@Override
	public String toString() {
		return ip + " => " + link;
	}
}
