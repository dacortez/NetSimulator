package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Router implements NetworkEvent {
	// Nome do roteador.
	private String name;
	// Número total de interfaces do roteadror.
	private int totalInterfaces; 
	// Lista de interfaces do roteador.
	private List<RouterInterface> interfaces;
	// Tempo em para processar um pacote em us.
	private double processingTime; 
	// Tabela de rotas: ip da subrede apontando para interface do roteador.
	private HashMap<Ip, RouterInterface> routes;
	
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
			iface.addNetworkEventListener(this);
		}
		routes = new HashMap<Ip, RouterInterface>();
	}
	
	public RouterInterface getInterface(int port) {
		return interfaces.get(port);
	}
	
	public RouterInterface getInterface(Ip ip) {
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
	
	@Override
	public void networkEventHandler(Interface sender, Datagram data) {
		System.out.println("Roteador " + name + " recebeu datagrama:");
		System.out.println(data);
		RouterInterface routedInterface = routes.get(data.getDestinationIp().subNetIp());
		if (routedInterface != null) { 
			System.out.println("[ROTEANDO PORTA " + routedInterface.getPort()  + "]\n");
			routedInterface.send(data);
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
