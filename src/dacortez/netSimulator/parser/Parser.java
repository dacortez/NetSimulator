package dacortez.netSimulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import dacortez.netSimulator.Event;
import dacortez.netSimulator.Interface;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Sniffer;
import dacortez.netSimulator.application.DnsServer;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.HttpClient;
import dacortez.netSimulator.application.HttpServer;
import dacortez.netSimulator.link.DuplexLink;
import dacortez.netSimulator.network.Router;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
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
	private List<Event> events;

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
	
	public List<Event> getEvents() {
		return events;
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
		events = new ArrayList<Event>();
	}
	
	public boolean parse() {
		clearCollections();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
				parseLine(line);
			reader.close();
			return true;
		} catch (IOException e) {
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
		events.clear();
	}
	
	private void parseLine(String line) {
		Matcher match = Regex.COMMENT.matcher(line);
		if (match.find()) {
			comment(match);
			return;
		}
		match = Regex.SET_HOST.matcher(line);
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
	
	private void comment(Matcher match) {
	}

	private void setHost(Matcher match) {
		String name = match.group(1);
		Host host = new Host(name);
		hosts.put(name, host);
		System.out.println("[Criado host " + host + "]");
	}
	
	private void setRouter(Matcher match) {
		String name = match.group(1);
		Integer totalInterfaces = Integer.parseInt(match.group(2)); 
		Router router = new Router(name, totalInterfaces);
		routers.put(name, router);
		System.out.println("[Criado router " + router + "]");
	}
	
	private void setDuplexLink(Matcher match) {
		Double capacity = Double.parseDouble(match.group(5));
		Double delay = Double.parseDouble(match.group(6));
		DuplexLink link = new DuplexLink(capacity, delay);
		System.out.println("[Criado duplex-link " + link + "]");
		setInterfaceLink(match.group(1), match.group(2), link);
		setInterfaceLink(match.group(3), match.group(4), link);
	}
	
	private void setInterfaceLink(String name, String portString, DuplexLink link) {
		if (portString != null) {
			Router router = routers.get(name);
			Integer port = Integer.parseInt(portString);
			router.getInterface(port).setLink(link);
			System.out.println("[Associa link " + link + " à porta " + port + " do router " + router + "]");
		}
		else {
			Host host = hosts.get(name);
			host.getInterface().setLink(link);
			System.out.println("[Associa link " + link + " ao host " + host + "]");
		}
	}
	
	private void setHostIps(Matcher match) {
		String name = match.group(1);
		Ip ip = new Ip(match.group(2));
		Ip standardRouterIp = new Ip(match.group(3));
		Ip dnsServerIp = new Ip(match.group(4));
		Host host = hosts.get(name);
		host.getInterface().setIp(ip);
		host.setStandardRouterIp(standardRouterIp);
		host.setDnsServerIp(dnsServerIp);
		System.out.println("[IPs do host " + host + ": " + ip + " " + standardRouterIp + " " + dnsServerIp + "]");
	}
	
	private void setRouterIps(Matcher match) {
		String name = match.group(1);
		String portIp[] = match.group(2).split("\\s+");
		Router router = routers.get(name);
		for (int i = 0; i < portIp.length; i += 2) {
			Integer port = Integer.parseInt(portIp[i]);
			Ip ip = new Ip(portIp[i + 1]);
			router.getInterface(port).setIp(ip); 
			System.out.println("[IP da porta " + port + " do router " + router + " configurado: " + ip + "]");
		}
	}
	
	private void setRouterPerformance(Matcher match) {
		String name = match.group(1);
		Double processingTime = Double.parseDouble(match.group(2));
		String[] queues = match.group(3).split("\\s+");
		Router router = routers.get(name);
		router.setProcessingTime(processingTime);
		System.out.println("[Tempo de processamento do router " + router + " configurado: " + router.getProcessingTime() + "]");
		for (int i = 0; i < queues.length; i += 2) {
			Integer port = Integer.parseInt(queues[i]);
			Integer queueSize = Integer.parseInt(queues[i + 1]);
			router.getInterface(port).setQueueSize(queueSize); 
			System.out.println("[Interface do router " + router + " configurada: " + router.getInterface(port) + "]");
		}
	}
	
	private void setHttpClient(Matcher match) {
		String name = match.group(1);
		HttpClient client = new HttpClient(name);
		httpClients.put(name, client);
		System.out.println("[Criado HTTP client " + client + "]");
	}
	
	private void setHttpServer(Matcher match) {
		String name = match.group(1);
		HttpServer server = new HttpServer(name);
		httpServers.put(name, server);
		System.out.println("[Criado HTTP server " + server + "]");
	}

	private void setDnsServer(Matcher match) {
		String name = match.group(1);
		DnsServer server = new DnsServer(name);
		dnsServers.put(name, server);
		System.out.println("[Criado DNS server " + server + "]");
	}
	
	private void attachAgent(Matcher match) {
		String agentName = match.group(1);
		String hostName = match.group(2);
		Host host = hosts.get(hostName);
		if (httpClients.get(agentName) != null) {
			httpClients.get(agentName).attach(host);
			System.out.println("[HTTP client " + agentName + " vinculado ao host " + hostName + "]");
		}
		else if (httpServers.get(agentName) != null) {
			httpServers.get(agentName).attach(host);
			System.out.println("[HTTP server " + agentName + " vinculado ao host " + hostName + "]");
		}
		else if (dnsServers.get(agentName) != null) {
			dnsServers.get(agentName).attach(host);
			System.out.println("[DNS server " + agentName + " vinculado ao host " + hostName + "]");
		}
	}
	
	private void setSniffer(Matcher match) {
		String name = match.group(1);
		Sniffer sniffer = new Sniffer(name);
		sniffers.put(name, sniffer);
		System.out.println("[Criado sniffer " + sniffer + "]");
	}
	
	private void attachSniffer(Matcher match) {
		String name = match.group(1);
		String file = match.group(6);
		Interface point1 = getInterface(match.group(2), match.group(3));
		Interface point2 = getInterface(match.group(4), match.group(5));
		Sniffer sniffer = sniffers.get(name);
		sniffer.setPoint1(point1);
		sniffer.setPoint1(point2);
		sniffer.setFile(file);
		System.out.println("[Sniffer " + name + " configurado entre " + point1 + " e " + point2 + "]");
		System.out.println("[Saída do sniffer " + name + ": " + file + "]");
	}
	
	private Interface getInterface(String name, String portString) {
		if (portString != null) {
			Router router = routers.get(name);
			Integer port = Integer.parseInt(portString);
			return router.getInterface(port);
		}
		Host host = hosts.get(name);
		return host.getInterface();
	}
	
	private void simEvent(Matcher match) {
		Double time = Double.parseDouble(match.group(1));
		String action = match.group(2);
		Event event = new Event(time, action);
		System.out.println("[Adicionado evento: " + event + "]");
	}
}
