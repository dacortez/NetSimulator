package dacortez.netSimulator.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Chronometer;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.application.dns.DnsAnswer;
import dacortez.netSimulator.application.dns.DnsMessage;
import dacortez.netSimulator.application.dns.DnsQuestion;
import dacortez.netSimulator.application.dns.RRType;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.transport.UdpSegment;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.12.01
 */
public class TracerouteProcess extends Process {
	// Host solicitado pelo processo.
	private String host;
	// Sinaliza que o processo está esperando pela resposta do servidor DNS.
	private boolean isWaitingDns;
	// Nome do host rodando o processo cliente HTTP.
	private String clientName;
	// Identificador das mensagens DNS.
	private int dnsId;
	// TTL dos datagramas gerados (começa com 1 e vai aumentando).
	private int ttl;
	// Horário de envio dos datagramas; 
	private double time;
	// Lista de IPs recebidos.
	private List<Ip> received;
	// Contador estático a ser utilizado na identificação das mensagens DNS.
	private static int count = 0;
	// PrintStream para saída do traceroute.
	private PrintStream ps;
	// Porta alta para onde os pacotes serão enviados. 
	private static final int DESTINATION_PORT = 33434;
	
	public void setTime(double time) {
		this.time = time;
	}
	
	public TracerouteProcess(Socket socket, String host, String clientName) {
		super(socket);
		this.host = host;
		this.clientName = clientName;
		isWaitingDns = true;
		ttl = 1;
		received = new ArrayList<Ip>();
		setupPrintStream();
	}
	
	public TracerouteProcess(Socket socket, String clientName) {
		super(socket);
		this.host = socket.getDestinationIp().getAddress();
		this.socket.setDestinationPort(DESTINATION_PORT);
		this.clientName = clientName;
		isWaitingDns = false;
		ttl = 1;
		received = new ArrayList<Ip>();
		setupPrintStream();
	}

	private void setupPrintStream() {
		if (!Simulator.experimentMode) {
			String name = "traceroute_" + clientName + "_pid_" + pid; 
			try {
				ps = new PrintStream(new File(name));
			} catch (FileNotFoundException e) {
				ps = null;
			}	
		}
	}
	
	@Override
	public Process fork() {
		return null;
	}

	@Override
	public Message request() {
		if (isWaitingDns) return dnsMessage();
		return null;
	}
	
	private DnsMessage dnsMessage() {
		dnsId = ++count;
		DnsMessage message = new DnsMessage(dnsId, false);
		message.addQuestion(new DnsQuestion(host, RRType.A));
		return message;
	}

	@Override
	public Message respond(Message message) {
		if (isWaitingDns) {
			DnsMessage reply = (DnsMessage) message;
			if (reply.getId() == dnsId) {
				Ip hostIp = getIp(reply);
				if (hostIp != null) {
					socket.setDestinationIp(hostIp);
					socket.setDestinationPort(DESTINATION_PORT);
				}
			}
		}
		return null;
	}

	private Ip getIp(DnsMessage reply) {
		if (reply.isReply() && !reply.getAnswears().isEmpty()) {
			DnsAnswer answer = reply.getAnswears().get(0);
			return new Ip(answer.getValue());
		}
		return null;
	}
	
	public Datagram next() {
		Integer sourcePort = socket.getSourcePort();
		Ip sourceIp = socket.getSourceIp();
		Integer destinationPort = socket.getDestinationPort();
		Ip destinationIp = socket.getDestinationIp();
		UdpSegment segment = new UdpSegment(sourcePort, destinationPort);
		Datagram next = new Datagram(segment, sourceIp, destinationIp);
		next.setTtl(ttl);
		return next;
	}
	
	public void incrementTtl() {
		ttl++;
	}

	public boolean icmpReceive(Ip ip) {
		if (!received.contains(ip)) {
			double now = Chronometer.getTime();
			double rtt = 1000.0 * (now - this.time);
			System.out.printf("%16s | RTT = %8.4f ms\n", ip, rtt);
			if (ps != null) ps.printf("%16s | RTT = %8.4f ms\n", ip, rtt);
			received.add(ip);
			return true;
		}
		return false;
	}
}
