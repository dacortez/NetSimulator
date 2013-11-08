package dacortez.netSimulator;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.DuplexLink;
import dacortez.netSimulator.network.NetworkEvent;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public abstract class Interface implements NetworkEvent {
	// Endereço IP da interface.
	protected Ip ip;
	// Enlace ao qual a interface está conectada.
	protected DuplexLink link;
	// Coleção de classes registradas ao evento NetworkEvent (sniffers e outras interfaces).
	private List<NetworkEvent> networkEventListeners;

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
	
	public void addNetworkEventListener(NetworkEvent listener) {
		if (networkEventListeners == null)
			networkEventListeners = new ArrayList<NetworkEvent>();
		networkEventListeners.add(listener);
	}
	
	public void fireNetworkEvent(Datagram data) {
		System.out.println("Interface " + ip + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[ENVIANDO DATAGRAMA VIA LINK]\n");
		for (NetworkEvent listener: networkEventListeners)
			listener.networkEventHandler(data);
	}
	
	@Override
	public abstract void networkEventHandler(Datagram data);
	
	@Override
	public String toString() {
		return ip + " => " + link;
	}
}
