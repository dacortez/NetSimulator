package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.DataQueuedInLink;


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
	protected List<EventArgs> linkQueue;
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
	
	public void linkQueueing(EventArgs args) {
		Datagram data = args.getDatagram();
		double timeAtDestination = getTimeAtDestination(args);
		EventArgs destination = new EventArgs(data, timeAtDestination);
		linkQueue.add(args);
		debugLinkQueue(timeAtDestination);
		Simulator.addToQueue(new DataQueuedInLink(this, destination));
	}

	private void debugLinkQueue(double timeAtDestination) {
		if (Simulator.debugMode) {
			System.out.println("**************************************************************");
			System.out.println("Interface " + ip + " LINK_QUEUE: " + linkQueue.size());
			System.out.println("Time at destination: " + timeAtDestination);
			System.out.println("**************************************************************\n");
		}
	}
	
	private double getTimeAtDestination(EventArgs in) {
		Datagram data = in.getDatagram();
		double inTime = in.getTime();
		double wait = link.getTransmissionTime(data.getNumberOfBytes());
		if (linkQueue.isEmpty())
			return inTime + wait;
		double first = Double.MAX_VALUE;
		for (EventArgs args: linkQueue) {
			if (args.getTime() < first)
				first = args.getTime();
			wait += link.getTransmissionTime(args.getDatagram().getNumberOfBytes());
		}
		return first + wait;
	}
	
	public void linkDequeueing(EventArgs args) {
		int index = -1;
		for (EventArgs queueArgs: linkQueue)
			if (queueArgs.getDatagram() == args.getDatagram())
				index = linkQueue.indexOf(queueArgs);
		if (index != -1) {
			linkQueue.remove(index);
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
