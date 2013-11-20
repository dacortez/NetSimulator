package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.OutboundData;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class Router {
	// Nome do roteador.
	private String name;
	// Lista de interfaces do roteador.
	private List<RouterInterface> interfaces;
	// Tempo para processar um pacote em us (microsegundos).
	private double processingDelay; 
	// Tabela de rotas: ip da subrede apontando para interface do roteador.
	private HashMap<Ip, RouterInterface> routes;
		
	public String getName() {
		return name;
	}

	public List<RouterInterface> getInterfaces() {
		return interfaces;
	}
	
	public double getProcessingDelay() {
		return processingDelay;
	}

	public void setProcessingDelay(double processingDelay) {
		this.processingDelay = processingDelay;
	}
	
	public Router(String name, int totalInterfaces) {
		this.name = name;
		interfaces = new ArrayList<RouterInterface>(totalInterfaces);
		for (int port = 0; port < totalInterfaces; port++) {
			RouterInterface routerInterface = new RouterInterface(this, port);
			interfaces.add(port, routerInterface);
		}
		routes = new HashMap<Ip, RouterInterface>();
	}
	
	public RouterInterface getRouterInterface(int port) {
		return interfaces.get(port);
	}
	
	public RouterInterface getRouterInterface(Ip ip) {
		for (RouterInterface ri: interfaces) 
			if (ri.getIp().equals(ip))
				return ri;
		return null;
	}
	
	public void addRoute(Ip from, Integer toPort) {
		for (RouterInterface ri: interfaces)
			if (ri.getPort() == toPort) {
				routes.put(from, ri);
				return;
			}
	}
		
	public void route(Datagram data, double time) {
		if (checkTtl(data)) {
			RouterInterface toInterface = routes.get(data.getDestinationIp().subNetIp());
			if (toInterface != null) { 
				double delayInSecs = processingDelay / 1000000.0;
				EventArgs args = new EventArgs(data, time + delayInSecs);
				Simulator.addToQueue(new OutboundData(toInterface, args));
			}
		}
	}

	private boolean checkTtl(Datagram data) {
		if (data.decrementTtl() == 0) {
			if (Simulator.debugMode) {
				System.out.println("--- TTL = 0 (pacote perdido) no roteador " + name + ":\n");
				System.out.println("DATAGRAM #" + data.getId());
			}
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(": ").append(processingDelay).append("us\n");
		appendInterfaces(sb);
		appendRoutes(sb);
		return sb.toString();
	}

	private void appendInterfaces(StringBuilder sb) {
		sb.append("  + Interfaces: ");
		for (RouterInterface ri: interfaces)
			sb.append(ri).append(", ");
		sb.deleteCharAt(sb.length() - 2);
		sb.append('\n');
	}
	
	private void appendRoutes(StringBuilder sb) {
		sb.append("  + Rotas: ");
		for (Ip from: routes.keySet())
			sb.append(from).append(" > ").append(routes.get(from).getPort()).append(", ");
		sb.deleteCharAt(sb.length() - 2);
	}
}
