package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.network.HostInterface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class Host {
	// Nome textual do hospedeiro.
	protected String name;
	// Interface do hospedeiro.
	protected HostInterface hostInterface;
	// Endereço IP do roteador padrão associado.
	protected Ip standardRouterIp;
	// Endereço IP do servidor DNS associado.
	protected Ip dnsServerIp;

	public String getName() {
		return name;
	}
	
	public HostInterface getHostInterface() {
		return hostInterface;
	}

	public void setHostInterface(HostInterface hostInterface) {
		this.hostInterface = hostInterface;
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
		hostInterface = new HostInterface();
	}
	
	public void attach(Host host) {
		name = host.getName();
		hostInterface = host.getHostInterface();
		hostInterface.setHost(this);
		standardRouterIp = host.getStandardRouterIp();
		dnsServerIp = host.getDnsServerIp();
	}
	
	public void receive(Message message) {
		System.out.println("Aplicação do host " + name + " recebeu menssagem:");
		System.out.println(message);
		System.out.println("[PROCESSANDO]\n");
	}
	
	@Override
	public String toString() {
		return name + ": (" + hostInterface + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
