package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.events.SimEventListener;

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
	private double processingTime; 
	// Tabela de rotas: ip da subrede apontando para interface do roteador.
	private HashMap<Ip, RouterInterface> routes;
	// Coleção de classes registradas ao evento SimEventListener (o simulator).
	private List<SimEventListener> listeners;
		
	public String getName() {
		return name;
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
		interfaces = new ArrayList<RouterInterface>(totalInterfaces);
		for (int port = 0; port < totalInterfaces; port++) {
			RouterInterface routerInterface = new RouterInterface(this, port);
			interfaces.add(port, routerInterface);
		}
		routes = new HashMap<Ip, RouterInterface>();
	}
	
	public void addSimEventListener(SimEventListener listener) {
		if (listeners == null)
			listeners = new ArrayList<SimEventListener>();
		listeners.add(listener);
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
	
	public void route(Datagram data) {
		System.out.println("Roteador " + name + " recebeu datagrama:");
		System.out.println(data);
		RouterInterface toInterface = routes.get(data.getDestinationIp().subNetIp());
		if (toInterface != null) { 
			System.out.println("[ROTEANDO PARA PORTA " + toInterface.getPort()  + "]\n");
			toInterface.fireNetworkEvent(data);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(": ").append(processingTime).append("us\n");
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
