package dacortez.netSimulator.application;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.transport.ServiceProvider;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class Host {
	// Nome textual do hospedeiro.
	protected String name;
	// Endereço IP do roteador padrão associado.
	protected Ip standardRouterIp;
	// Endereço IP do servidor DNS associado.
	protected Ip dnsServerIp;
	// Provedor de serviços da camada de transporte associado.
	protected ServiceProvider serviceProvider;
	// Lista de processos ativos do hospedeiro.
	protected List<Thread> threads;

	public String getName() {
		return name;
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
	
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	
	public Host() {
	}
		
	public Host(String name) {
		this.name = name;
		serviceProvider = new ServiceProvider();
	}
	
	public void attach(Host host) {
		name = host.getName();
		standardRouterIp = host.getStandardRouterIp();
		dnsServerIp = host.getDnsServerIp();
		serviceProvider = host.getServiceProvider();
		serviceProvider.setHost(this);
		threads = new ArrayList<Thread>();
	}
	
	public void receive(Message message) {
		System.out.println("Aplicação do host " + name + " recebeu menssagem:");
		System.out.println(message);
		System.out.println("[PROCESSANDO]\n");
	}
	
	@Override
	public String toString() {
		return name + ": (" + serviceProvider + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
