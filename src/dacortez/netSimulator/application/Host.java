package dacortez.netSimulator.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import dacortez.netSimulator.Chronometer;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
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
		setupPrintStream();
	}

	private void setupPrintStream() {
		if (!Simulator.experimentMode) {
			try {
				ps = new PrintStream(new File(name));
			} catch (FileNotFoundException e) {
				System.err.println("Erro ao criar arquivo com os dados recebidos pelo host " + name + ".");
				System.err.println("(Os dados serão apresentados apenas na saída padrão).");
				ps = null;
			}
		}
	}
	
	public void addProcess(Process process) {
		processes.add(process);
	}
	
	public void receive(Message message, Process process) {
		
	}

	protected void print(String name, Message message) {
		if (!Simulator.experimentMode) {
			StringBuilder sb = new StringBuilder();
			sb.append("===============================================================================================\n");
			sb.append("[Time = " + Chronometer.getTime() + "]\n");
			sb.append("Host " + name + " recebeu uma mensagem:\n");
			sb.append(message);
			sb.append("\n===============================================================================================\n");
			if (Simulator.debugMode) System.out.println(sb.toString());
			if (ps != null) ps.println(sb.toString());
		}
	}
	
	@Override
	public String toString() {
		return name + ": (" + serviceProvider + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
