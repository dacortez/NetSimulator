package dacortez.netSimulator;

import dacortez.netSimulator.link.DuplexLink;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Interface {
	// Endereço IP da interface.
	protected Ip ip;
	// Enlace ao qual a interface está conectada.
	protected DuplexLink link;
	
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
	
	@Override
	public String toString() {
		return ip + " => " + link;
	}
}
