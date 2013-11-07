package dacortez.netSimulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import dacortez.netSimulator.SimEvent;
import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Sniffer;
import dacortez.netSimulator.application.DnsServer;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.HttpClient;
import dacortez.netSimulator.application.HttpServer;
import dacortez.netSimulator.link.DuplexLink;
import dacortez.netSimulator.network.Router;
import dacortez.netSimulator.network.RouterInterface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class Parser {
	// Nome do arquivo a ser parseado.
	private String file;
	// HashMap de hospedeiros instanciados durante o parseamento.
	private HashMap<String, Host> hosts;  
	// HashMap de servidores DNS instanciados durante o parseamento.
	private HashMap<String, DnsServer> dnsServers;
	// HashMap de clientes HTTP instanciados durante o parseamento.
	private HashMap<String, HttpClient> httpClients;
	// HashMap de servidores HTTP instanciados durante o parseamento.
	private HashMap<String, HttpServer> httpServers;
	// HashMap de roteadores instanciados durante o parseamento.
	private HashMap<String, Router> routers;
	// Lista de duplex-links instanciados durante o parseamento.
	private List<DuplexLink> links;
	// HashMap de sniffers instanciados durante o parseamento.
	private HashMap<String, Sniffer> sniffers;
	// Lista dos eventos a serem processados pelo simulador.
	private List<SimEvent> simEvents;

	public String getFile() {
		return file;
	}
	
	public HashMap<String, DnsServer> getDnsServers() {
		return dnsServers;
	}

	public HashMap<String, HttpClient> getHttpClients() {
		return httpClients;
	}

	public HashMap<String, HttpServer> getHttpServers() {
		return httpServers;
	}

	public HashMap<String, Router> getRouters() {
		return routers;
	}

	public List<DuplexLink> getLinks() {
		return links;
	}
	
	public HashMap<String, Sniffer> getSniffer() {
		return sniffers;
	}
	
	public List<SimEvent> getSimEvents() {
		return simEvents;
	}
	
	public Parser(String file) {
		this.file = file;
		hosts = new HashMap<String, Host>();
		dnsServers = new HashMap<String, DnsServer>();
		httpClients = new HashMap<String, HttpClient>();
		httpServers = new HashMap<String, HttpServer>();
		routers = new HashMap<String, Router>();
		links = new ArrayList<DuplexLink>();
		sniffers = new HashMap<String, Sniffer>();
		simEvents = new ArrayList<SimEvent>();
	}
	
	public boolean parse() {
		clearCollections();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null)
				parseLine(line);
			reader.close();
			return true;
		} catch (Exception e) {
			System.out.println("Ocorreu um erro na tradução do arquivo de entrada: " + e.getMessage());
			System.out.println("O problema ocorreu na linha: " + line);
			System.out.println("Não será possível realizar a simulação.");
			e.printStackTrace();
			return false;
		}
	}
	
	private void clearCollections() {
		hosts.clear();
		dnsServers.clear();
		httpClients.clear();
		httpServers.clear();
		routers.clear();
		links.clear();
		sniffers.clear();
		simEvents.clear();
	}
	
	private void parseLine(String line) {
		Matcher match = Regex.SET_HOST.matcher(line);
		if (match.find()) {
			setHost(match);
			return;
		}
		match = Regex.SET_ROUTER.matcher(line);
		if (match.find()) {
			setRouter(match);
			return;
		}
		match = Regex.DUPLEX_LINK.matcher(line);
		if (match.find()) {
			setDuplexLink(match);			
			return;
		}
		match = Regex.HOST_IPS.matcher(line);
		if (match.find()) {
			setHostIps(match);			
			return;
		}
		match = Regex.ROUTER_IPS.matcher(line);
		if (match.find()) {
			setRouterIps(match);			
			return;
		}
		match = Regex.ROUTER_ROUTE.matcher(line);
		if (match.find()) {
			setRouterRoute(match);			
			return;
		}
		match = Regex.ROUTER_PERFORMANCE.matcher(line);
		if (match.find()) {
			setRouterPerformance(match);			
			return;
		}
		match = Regex.SET_HTTP_CLIENT.matcher(line);
		if (match.find()) {
			setHttpClient(match);			
			return;
		}
		match = Regex.SET_HTTP_SERVER.matcher(line);
		if (match.find()) {
			setHttpServer(match);			
			return;
		}
		match = Regex.SET_DNS_SERVER.matcher(line);
		if (match.find()) {
			setDnsServer(match);			
			return;
		}
		match = Regex.ATTACH_AGENT.matcher(line);
		if (match.find()) {
			attachAgent(match);			
			return;
		}
		match = Regex.SET_SNIFFER.matcher(line);
		if (match.find()) {
			setSniffer(match);			
			return;
		}
		match = Regex.ATTACH_SNIFFER.matcher(line);
		if (match.find()) {
			attachSniffer(match);			
			return;
		}
		match = Regex.SIM_EVENT.matcher(line);
		if (match.find()) {
			simEvent(match);			
			return;
		}
	}
	
	private void setHost(Matcher match) {
		String name = match.group(1);
		Host host = new Host(name);
		hosts.put(name, host);
		System.out.println("[Criado host: " + name + "]");
	}
	
	private void setRouter(Matcher match) {
		String name = match.group(1);
		Integer totalInterfaces = Integer.parseInt(match.group(2)); 
		Router router = new Router(name, totalInterfaces);
		routers.put(name, router);
		System.out.println("[Criado roteador: " + name + "]");
	}
	
	private void setDuplexLink(Matcher match) {
		Double capacity = Double.parseDouble(match.group(5));
		Double delay = Double.parseDouble(match.group(6));
		DuplexLink link = new DuplexLink(capacity, delay);
		links.add(link);
		System.out.println("[Criado duplex-link: " + link + "]");
		setInterfaceLink(match.group(1), match.group(2), link);
		setInterfaceLink(match.group(3), match.group(4), link);
		setListeners(match);
	}
	
	private void setInterfaceLink(String name, String portString, DuplexLink link) {
		if (portString != null) {
			Router router = routers.get(name);
			Integer port = Integer.parseInt(portString);
			router.getRouterInterface(port).setLink(link);
			System.out.println("[Associado link " + link + " à porta " + port + " do roteador " + name + "]");
		}
		else {
			Host host = hosts.get(name);
			host.getHostInterface().setLink(link);
			System.out.println("[Associado link " + link + " ao host " + name + "]");
		}
	}
	
	private void setListeners(Matcher match) {
		Interface iface1 = getInterfaceFrom(match.group(1), match.group(2));
		Interface iface2 = getInterfaceFrom(match.group(3), match.group(4));
		iface1.addLinkEventListener(iface2);
		iface2.addLinkEventListener(iface1);
	}
	
	private Interface getInterfaceFrom(String name, String portString) {
		if (portString != null) {
			Router router = routers.get(name);
			Integer port = Integer.parseInt(portString);
			return router.getRouterInterface(port);
		}
		Host host = hosts.get(name);
		return host.getHostInterface();
	}
	
	private void setHostIps(Matcher match) {
		String name = match.group(1);
		Ip ip = new Ip(match.group(2));
		Ip standardRouterIp = new Ip(match.group(3));
		Ip dnsServerIp = new Ip(match.group(4));
		Host host = hosts.get(name);
		host.getHostInterface().setIp(ip);
		host.setStandardRouterIp(standardRouterIp);
		host.setDnsServerIp(dnsServerIp);
		System.out.println("[Configurado IPs do host " + name + ": " + ip + ", " + standardRouterIp + ", " + dnsServerIp + "]");
	}
	
	private void setRouterIps(Matcher match) {
		String name = match.group(1);
		String portIp[] = match.group(2).split("\\s+");
		Router router = routers.get(name);
		for (int i = 0; i < portIp.length; i += 2) {
			Integer port = Integer.parseInt(portIp[i]);
			Ip ip = new Ip(portIp[i + 1]);
			router.getRouterInterface(port).setIp(ip); 
			System.out.println("[Configurado IP da porta " + port + " do roteador " + name + ": " + ip + "]");
		}
	}
	
	private void setRouterRoute(Matcher match) {
		String name = match.group(1);
		String route[] = match.group(2).split("\\s+");
		Router router = routers.get(name);
		for (int i = 0; i < route.length; i += 2) {
			Ip from = new Ip(route[i]);
			if (route[i + 1].contains(".")) {
				Ip toIp = new Ip(route[i + 1]);
				Integer to = getPortConnectedToIp(router, toIp);
				router.addRoute(from, to);
				System.out.println("[Configurada rota do roteador " + name + ": " + from + " > " + toIp + "]");
			}
			else {
				Integer to = Integer.parseInt(route[i + 1]);
				router.addRoute(from, to);
				System.out.println("[Configurada rota do roteador " + name + ": " + from + " > " + to + "]");
			}
		}
	}
	
	private Integer getPortConnectedToIp(Router router, Ip to) {
		for (Router other: routers.values()) {
			RouterInterface otherInterface = other.getRouterInterface(to);
			if (otherInterface != null) {
				DuplexLink link = otherInterface.getLink();
				for (RouterInterface ri: router.getInterfaces())
					if (link == ri.getLink())
						return ri.getPort();
			}
		}
		return -1;
	}

	private void setRouterPerformance(Matcher match) {
		String name = match.group(1);
		Double processingTime = Double.parseDouble(match.group(2));
		String[] queues = match.group(3).split("\\s+");
		Router router = routers.get(name);
		router.setProcessingTime(processingTime);
		System.out.println("[Configurado tempo de processamento do roteador " + name + ": " + processingTime + "us]");
		for (int i = 0; i < queues.length; i += 2) {
			Integer port = Integer.parseInt(queues[i]);
			Integer queueSize = Integer.parseInt(queues[i + 1]);
			router.getRouterInterface(port).setQueueSize(queueSize); 
			System.out.println("[Configurado tamanha da fila da porta " + port + " do roteador " + name + ": " + queueSize + "]");
		}
	}
	
	private void setHttpClient(Matcher match) {
		String name = match.group(1);
		HttpClient client = new HttpClient(name);
		httpClients.put(name, client);
		System.out.println("[Criado cliente HTTP: " + name + "]");
	}
	
	private void setHttpServer(Matcher match) {
		String name = match.group(1);
		HttpServer server = new HttpServer(name);
		httpServers.put(name, server);
		System.out.println("[Criado servidor HTTP: " + name + "]");
	}

	private void setDnsServer(Matcher match) {
		String name = match.group(1);
		DnsServer server = new DnsServer(name);
		dnsServers.put(name, server);
		System.out.println("[Criado servidor DNS: " + name + "]");
	}
	
	private void attachAgent(Matcher match) {
		String agentName = match.group(1);
		String hostName = match.group(2);
		Host host = hosts.get(hostName);
		if (httpClients.get(agentName) != null) {
			httpClients.get(agentName).attach(host);
			System.out.println("[Cliente HTTP " + agentName + " vinculado ao host " + hostName + "]");
		}
		else if (httpServers.get(agentName) != null) {
			httpServers.get(agentName).attach(host);
			System.out.println("[Servidor HTTP " + agentName + " vinculado ao host " + hostName + "]");
		}
		else if (dnsServers.get(agentName) != null) {
			dnsServers.get(agentName).attach(host);
			System.out.println("[Servidor DNS " + agentName + " vinculado ao host " + hostName + "]");
		}
	}
	
	private void setSniffer(Matcher match) {
		String name = match.group(1);
		Sniffer sniffer = new Sniffer(name);
		sniffers.put(name, sniffer);
		System.out.println("[Criado sniffer: " + name + "]");
	}
	
	private void attachSniffer(Matcher match) {
		String name = match.group(1);
		String file = match.group(6);
		Interface point1 = getInterfaceFrom(match.group(2), match.group(3));
		Interface point2 = getInterfaceFrom(match.group(4), match.group(5));
		Sniffer sniffer = sniffers.get(name);
		sniffer.setPoint1(point1);
		sniffer.setPoint2(point2);
		sniffer.setFile(file);
		point1.addLinkEventListener(sniffer);
		point2.addLinkEventListener(sniffer);
		String p1 = getIpAndPort(point1);
		String p2 = getIpAndPort(point2);
		System.out.println("[Sniffer " + name + " configurado entre " + p1 + " e " + p2 + "]");
		System.out.println("[Configurado arquivo de saída do sniffer " + name + ": " + file + "]");
	}
	
	private String getIpAndPort(Interface iface) {
		if (iface instanceof RouterInterface) {
			RouterInterface ri = (RouterInterface) iface;
			return ri.getIp() + ":" + ri.getPort();
		}
		return iface.getIp().toString();
	}
	
	private void simEvent(Matcher match) {
		Double time = Double.parseDouble(match.group(1));
		String action = match.group(2);
		SimEvent simEvent = new SimEvent(time, action);
		simEvents.add(simEvent);
		System.out.println("[Adicionado evento: " + simEvent + "]");
	}
	
	public void printElements() {
		printLinks();
		printHosts();
		printDnsServers();
		printHttpServers();
		printHttpClients();
		printRouters();
		printSniffers();
		printSimEvents();
		System.out.println();
	}
	
	private void printLinks() {
		System.out.println("----------- Lista de Duplex-Links ------------");
		for (DuplexLink link: links)
			System.out.println(link);
	}
	
	private void printHosts() {
		System.out.println("------------ Lista de Hosts ------------");
		for (Host host: hosts.values())
			System.out.println(host);
	}
	
	private void printDnsServers() {
		System.out.println("------------ Lista de Servidores DNS ------------");
		for (DnsServer dnsServer: dnsServers.values())
			System.out.println(dnsServer);
	}
	
	private void printHttpServers() {
		System.out.println("------------ Lista de Servidores HTTP ------------");
		for (HttpServer httpServer: httpServers.values())
			System.out.println(httpServer);
	}
	
	private void printHttpClients() {
		System.out.println("------------ Lista de Clientes HTTP ------------");
		for (HttpClient httpClient: httpClients.values())
			System.out.println(httpClient);
	}
	
	private void printRouters() {
		System.out.println("------------ Lista de Roteadores ------------");
		for (Router router: routers.values())
			System.out.println(router);
	}
	
	private void printSniffers() {
		System.out.println("------------ Lista de Sniffers ------------");
		for (Sniffer sniffer: sniffers.values())
			System.out.println(sniffer);
	}
	
	private void printSimEvents() {
		System.out.println("------------ Lista de Eventos ------------");
		for (SimEvent simEvent: simEvents)
			System.out.println(simEvent);
	}
}
