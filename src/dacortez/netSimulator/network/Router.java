package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Router {
	// Nome do roteador.
	private String name;
	// Número total de interfaces do roteadror.
	private int totalInterfaces; 
	// Lista de interfaces do roteador.
	private List<RouterInterface> interfaces;
	// Tempo em para processar um pacote em us.
	private double processingTime; 
	
	public String getName() {
		return name;
	}

	public int getTotalInterfaces() {
		return totalInterfaces;
	}

	public List<RouterInterface> getInterfaces() {
		return interfaces;
	}
	
	public double getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(double processingTime) {
		this.processingTime = processingTime;
	}
	
	public Router(String name, int totalInterfaces) {
		this.name = name;
		this.totalInterfaces = totalInterfaces;
		interfaces = new ArrayList<RouterInterface>(totalInterfaces);
		for (int port = 0; port < totalInterfaces; port++) {
			RouterInterface iface = new RouterInterface(port);
			interfaces.add(port, iface);
		}
	}
	
	public RouterInterface getInterface(int port) {
		return interfaces.get(port);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(": ").append(processingTime).append("us; ");
		for (RouterInterface ri: interfaces)
			sb.append(ri).append(", ");
		return sb.toString().substring(0, sb.length() - 2);
	}
}
