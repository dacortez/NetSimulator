package dacortez.netSimulator;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.link.DuplexLink;
import dacortez.netSimulator.link.LinkEvent;
import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public abstract class Interface implements LinkEvent {
	// Endereço IP da interface.
	protected Ip ip;
	// Enlace ao qual a interface está conectada.
	protected DuplexLink link;
	// Coleção de classes registradas ao evento LinkEvent (sniffers e outras interfaces).
	private List<LinkEvent> linkEventListeners;

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
	
	public void addLinkEventListener(LinkEvent listener) {
		if (linkEventListeners == null)
			linkEventListeners = new ArrayList<LinkEvent>();
		linkEventListeners.add(listener);
	}
	
	public void send(Datagram data) {
		for (LinkEvent listener: linkEventListeners)
			listener.linkEventHandler(data);
	}
	
	@Override
	public String toString() {
		return ip + " => " + link;
	}
}
