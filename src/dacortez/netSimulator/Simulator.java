package dacortez.netSimulator;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;

import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.dns.DnsServer;
import dacortez.netSimulator.application.http.HttpClient;
import dacortez.netSimulator.application.http.HttpServer;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.Finish;
import dacortez.netSimulator.events.SimEvent;
import dacortez.netSimulator.parser.Parser;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Simulator {
	// Objeto responsável pela leitura do arquivo de entrada 
	// e construção dos objetos da simulação (a rede).
	private Parser parser;
	// Lista de eventos que ocorrem na rede da simulação que 
	// devem ser processados em ordem (tempo da simulação).
	private static Queue<SimEvent> queue;
	// Variável indicando se o modo de depuração está ativo.
	public static boolean debugMode = false;
	// Variável indicando se os dados das mensagens devem ser impressos.
	public static boolean printData = true;
	// Variável indicando se a simulação está sendo feita para realizar o experimento do EP.
	public static boolean experimentMode = false;
	// Número de mediadas a serem realizadas no experimento.
	public static final int TRIALS = 30;

	public static void main(String[] args) {
		if (args.length < 1) {
			printHelp();
			return;
		}
		setOptions(args);
		chooseMode(args[0]);
	}
	
	private static void printHelp() {
		System.out.println("Uso: java -jar netSimulator.jar <arquivo_de_entrada.ns> <opção>");
		System.out.println("Onde as opções válidas são:");
		System.out.println("  -e    Realiza experimentos do EP em modo silencioso");
		System.out.println("  -n    Não exibe dados das mensagens geradas na simulação");
		System.out.println("  -d    Exibe informações de depuração ao longo da simulação");
	}

	private static void setOptions(String[] args) {
		if (args.length == 2) {
			if (args[1].contentEquals("-n")) {
				experimentMode = false;
				debugMode = false;
				printData = false;
			}
			else if (args[1].contentEquals("-d")) {
				experimentMode = false;
				debugMode = true;
				printData = true;
			}
			else if (args[1].contentEquals("-e")) {
				experimentMode = true;
				debugMode = false;
				printData = false;
			}
		}
	}
	
	private static void chooseMode(String file) {
		if (experimentMode) {
			doExperiment(file);
		}
		else { 
			Simulator sim = new Simulator(file);
			sim.simulate();
		}
	}
	
	private static void doExperiment(String file) {
		JavaSysMon monitor = new JavaSysMon();
		double[] real = new double[TRIALS];
		double[] cpu = new double[TRIALS];
		for (int i = 0; i < TRIALS; i++) {
			System.out.println("REALIZANDO SIMULAÇÃO " + (i + 1));
			CpuTimes previous = monitor.cpuTimes();
			double start = System.currentTimeMillis();
			Simulator simulator = new Simulator(file);
			simulator.simulate();
			cpu[i] = 100.0 * (monitor.cpuTimes().getCpuUsage(previous));
			real[i] = (System.currentTimeMillis() - start) / 1000.0;
		}
		printSummary(real, cpu);
	}

	private static void printSummary(double[] real, double[] cpu) {
		System.out.println("Média de tempo real = " + average(real) + " s");
		System.out.println("Desvio-padrão = " + stdv(real) + " s");
		System.out.println("Média de utilização de CPU = " + average(cpu) + " %");
		System.out.println("Desvio-padrão = " + stdv(cpu) + " %");
	}

	private static double average(double[] x) {
		double sum = 0.0;
		for (int i = 0; i < x.length; i++)
			sum += x[i];
		return sum / x.length;
	}
	
	private static double stdv(double[] x) {
		double avg = average(x);
		double sum = 0.0;
		for (int i = 0; i < x.length; i++)
			sum += (x[i] - avg) * (x[i] - avg);
		return Math.sqrt(sum / x.length);
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
		queue.clear();
		if (parser.parse()) {
			if (!experimentMode) parser.printElements();
			setupDnsServers();
			startAllServers();
			processHostActions();
			processEventsQueue();
		}
	}
	
	public void setupDnsServers() {
		for (DnsServer dnsServer: parser.getDnsServers())
			for (Host host: parser.getHosts())
				dnsServer.addHost(host.getName(), host.getIp());
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

	private void processHostActions() {
		Collection<HostAction> actions = parser.getHostActions();
		for (HostAction action: actions)
			if (action.isGet())
				clientGet(action);
			else if (action.isTraceroute())
				clientTraceroute(action);
			else if (action.isFinish())
				queue.add(new Finish(new EventArgs(action.getTime())));
	}

	private void clientGet(HostAction action) {
		String host = action.getHost();
		String target = action.getTarget();
		String resource = action.getResource();
		HttpClient client = parser.getHttpClient(host);
		if (client != null) {
			Chronometer.setTime(action.getTime());
			client.get(target, resource);
		}
	}
	
	private void clientTraceroute(HostAction action) {
		String host = action.getHost();
		String target = action.getTarget();
		HttpClient client = parser.getHttpClient(host);
		if (client != null) {
			Chronometer.setTime(action.getTime());
			client.traceroute(target);
		}
	}
	
	private void processEventsQueue() {
		while (!queue.isEmpty()) {
			SimEvent e = queue.poll();
			if (debugMode) 
				System.out.println("(-) Evento retirado da fila do simulador:\n" + e);
			e.fire();
			if (e instanceof Finish) break;
		}	
	}

	public static void addToQueue(SimEvent e) {
		queue.add(e);
		if (debugMode) 
			System.out.println("(+) Evento adicionado à fila do simulador:\n" + e);
	}
}
