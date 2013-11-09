package dacortez.netSimulator.application;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.process.Process;
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
	// Lista de processos em execução no hospedeiro.
	protected List<Process> processes;

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
	
	public List<Process> getProcesses() {
		return processes;
	}
	
	public Host() {
	}
		
	public Host(String name) {
		this.name = name;
		serviceProvider = new ServiceProvider();
	}
	
	public Ip getIp() {
		return serviceProvider.getHostInterface().getIp();
	}
	
	public void attach(Host host) {
		name = host.getName();
		standardRouterIp = host.getStandardRouterIp();
		dnsServerIp = host.getDnsServerIp();
		serviceProvider = host.getServiceProvider();
		serviceProvider.setHost(this);
		processes = new ArrayList<Process>();
	}
	
	public void receive(Message message, Process process) {
		if (process != null) {
			System.out.println("Aplicação do host " + name + " recebeu menssagem:");
			System.out.println(message);
		}
	}
	
	@Override
	public String toString() {
		return name + ": (" + serviceProvider + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
