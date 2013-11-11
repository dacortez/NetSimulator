package dacortez.netSimulator;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import dacortez.netSimulator.application.DnsServer;
import dacortez.netSimulator.application.HttpClient;
import dacortez.netSimulator.application.HttpServer;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.Finish;
import dacortez.netSimulator.events.OutHost;
import dacortez.netSimulator.events.SimEvent;
import dacortez.netSimulator.events.SimEventListener;
import dacortez.netSimulator.network.Router;
import dacortez.netSimulator.parser.Parser;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Simulator implements SimEventListener {
	// Objeto responsável pela leitura do arquivo de entrada 
	// e construção dos objetos da simulação (a rede).
	private Parser parser;
	// Lista de eventos que ocorrem na rede da simulação que 
	// devem ser processados em ordem (tempo da simulação).
	private Queue<SimEvent> queue;
	// Tamanho máximo da fila de eventos que o simulador pode suportar.
	// private int MAX_QUEUE_SIZE = 1;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java -jar netSimulator.jar <arquivo_de_entrada.ns>");
			return;
		}
		Simulator sim = new Simulator(args[0]);
		sim.simulate();
	}
	
	public Simulator(String file) {
		parser = new Parser(file);
		queue = new PriorityQueue<SimEvent>(1, new Comparator<SimEvent>() {
			@Override
			public int compare(SimEvent e1, SimEvent e2) {
				return e1.getEventArgs().getTime() < e2.getEventArgs().getTime() ? -1 : 1;
			}
		});
	}
	
	public void simulate() {
		if (parser.parse()) {
			parser.printElements();
			linkElementsToSimulator();
			startAllServers();
			//parser.getHttpClient("httpc1").get("h2");
			putHostActionsIntoQueue();
			processQueue();
		}
	}

	private void linkElementsToSimulator() {
		linkDnsServersToSimulator();
		linkHttpServersToSimulator();
		linkHttpClientsToSimulator();
		linkRoutersToSimulator();
	}
	
	private void linkDnsServersToSimulator() {
		Collection<DnsServer> servers = parser.getDnsServers();
		for (DnsServer server: servers)
			server.addSimEventListener(this);
	}

	private void linkHttpServersToSimulator() {
		Collection<HttpServer> servers = parser.getHttpServers();
		for (HttpServer server: servers)
			server.addSimEventListener(this);
	}

	private void linkHttpClientsToSimulator() {
		Collection<HttpClient> clients = parser.getHttpClients();
		for (HttpClient client: clients)
			client.addSimEventListener(this);
	}

	private void linkRoutersToSimulator() {
		Collection<Router> routers = parser.getRouters();
		for (Router router: routers)
			router.addSimEventListener(this);
	}

	private void startAllServers() {
		startDnsServers();
		startHttpServers();
	}

	private void startDnsServers() {
		Collection<DnsServer> servers = parser.getDnsServers();
		for (DnsServer server: servers)
			server.start();	
	}
	
	private void startHttpServers() {
		Collection<HttpServer> servers = parser.getHttpServers();
		for (HttpServer server: servers)
			server.start();
	}

	private void putHostActionsIntoQueue() {
		// TODO Auto-generated method stub
		// Cria eventos associados as ações dos hosts no parser e coloca na fila.
		queue.add(new OutHost(null, new EventArgs(null, 1.0)));
		queue.add(new OutHost(null, new EventArgs(null, 2.0)));
		queue.add(new OutHost(null, new EventArgs(null, 0.1)));
		queue.add(new OutHost(null, new EventArgs(null, 1.5)));
		queue.add(new Finish(new EventArgs(null, 1.9)));
	}
	
	private void processQueue() {
		while (!queue.isEmpty()) {
			SimEvent e = queue.poll();
			System.out.println(e);
			e.fire();
			if (e instanceof Finish) break;
		}	
	}

	@Override
	public void simEventHandler(SimEvent e) {
		queue.add(e);
	}
}
