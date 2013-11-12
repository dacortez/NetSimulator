package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.SimEvent;
import dacortez.netSimulator.events.SimEventListener;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public abstract class Interface implements NetworkEventListener {
	// Endereço IP da interface.
	protected Ip ip;
	// Enlace ao qual a interface está conectada.
	protected DuplexLink link;
	// Coleção de classes registradas ao evento SimEventListener (o simulator).
	private List<SimEventListener> simEventListeners;
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
	
	public void addSimEventListener(SimEventListener listener) {
		if (simEventListeners == null)
			simEventListeners = new ArrayList<SimEventListener>();
		simEventListeners.add(listener);
	}
	
	public void fireSimEvent(SimEvent e) {
		if (simEventListeners != null)
			for (SimEventListener listener: simEventListeners)
				listener.simEventHandler(e);
	}
	
	public void addNetworkEventListener(NetworkEventListener listener) {
		if (networkEventListeners == null)
			networkEventListeners = new ArrayList<NetworkEventListener>();
		networkEventListeners.add(listener);
	}
	
	public void fireNetworkEvent(EventArgs args) {
		if (networkEventListeners != null) {
			System.out.println("Interface " + ip + " recebeu datagrama:");
			System.out.println(args.getDatagram());
			System.out.println("[ENVIANDO DATAGRAMA VIA LINK]\n");
			for (NetworkEventListener listener: networkEventListeners)
				listener.networkEventHandler(args);
		}
	}
	
	@Override
	public abstract void networkEventHandler(EventArgs args);
	
	@Override
	public String toString() {
		return ip + " => " + link;
	}
}
