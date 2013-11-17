package dacortez.netSimulator.application;

import java.io.PrintStream;
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
	// Lista de processos gerenciados pelo host.
	protected List<Process> processes;
	// PrintStream utilizado para impressão de resultados.
	protected PrintStream ps;
	
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
	}
	
	public void addProcess(Process process) {
		processes.add(process);
	}
	
	public void receive(Message message, Process process) {
		System.out.println("Aplicação do host " + name + " recebeu uma mensagem:");
		System.out.println(message);
	}
	
	@Override
	public String toString() {
		return name + ": (" + serviceProvider + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
