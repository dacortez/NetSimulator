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
	// Rotas que apontam para uma porta.
	private HashMap<Ip, Integer> portForIp;
	// TODO
	// Rotas que apontam para outro roteador. (TIRAR ISSO E PRÉ-PROCESSAR)
	private HashMap<Ip, Ip> ipForIp;
	
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
		portForIp = new HashMap<Ip, Integer>();
		ipForIp = new HashMap<Ip, Ip>();
	}
	
	public RouterInterface getInterface(int port) {
		return interfaces.get(port);
	}
	
	public void addRoute(Ip from, Integer to) {
		portForIp.put(from, to);
	}
	
	public void addRoute(Ip from, Ip to) {
		ipForIp.put(from, to);
	}
	
	@Override
	public void networkEventHandler(Interface sender, Datagram data) {
		System.out.println("Roteador " + name + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[ROTEANDO]");
		RouterInterface ri = route(data.getDestinationIp());
		if (ri != null) 
			ri.send(data);
	}
	
	private RouterInterface route(Ip destinationIp) {
		Ip sub = destinationIp.subNetIp();
		if (portForIp.containsKey(sub)) {
			Integer port = portForIp.get(sub);
			for (RouterInterface ri: interfaces)
				if (ri.getPort() == port)
					return ri;
		}
		return null;
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
		for (Ip from: portForIp.keySet())
			sb.append(from).append(" > ").append(portForIp.get(from)).append(", ");
		for (Ip from: ipForIp.keySet())
			sb.append(from).append(" > ").append(ipForIp.get(from)).append(", ");
		sb.deleteCharAt(sb.length() - 2);
	}
}
