package dacortez.netSimulator.application;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.NetworkEvent;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Host implements NetworkEvent {
	// Nome textual do hospedeiro.
	protected String name;
	// Interface do hospedeiro.
	protected Interface iface;
	// Endereço IP do roteador padrão associado.
	protected Ip standardRouterIp;
	// Endereço IP do servidor DNS associado.
	protected Ip dnsServerIp;

	public String getName() {
		return name;
	}
	
	public Interface getInterface() {
		return iface;
	}

	public void setInterface(Interface iface) {
		this.iface = iface;
	}

	public Ip getStandardRouterIp() {
		return standardRouterIp;
	}

	public void setStandardRouterIp(Ip standardRouterIp) {
		this.standardRouterIp = standardRouterIp;
	}

	public Ip getDnsServerIp() {
		return dnsServerIp;
	}

	public void setDnsServerIp(Ip dnsServerIp) {
		this.dnsServerIp = dnsServerIp;
	}
	
	public Host() {
	}
		
	public Host(String name) {
		this.name = name;
		iface = new Interface();
	}
	
	public void attach(Host host) {
		name = host.getName();
		iface = host.getInterface();
		standardRouterIp = host.getStandardRouterIp();
		dnsServerIp = host.getDnsServerIp();
	}
	
	@Override
	public void networkEventHandler(Interface sender, Datagram data) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public String toString() {
		return name + ": (" + iface + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
