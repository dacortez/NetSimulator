package dacortez.netSimulator.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import dacortez.netSimulator.Chronometer;
import dacortez.netSimulator.Ip;
import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.application.dns.DnsAnswer;
import dacortez.netSimulator.application.dns.DnsMessage;
import dacortez.netSimulator.application.dns.DnsQuestion;
import dacortez.netSimulator.application.dns.RRType;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.transport.IcmpSegment;
import dacortez.netSimulator.transport.UdpSegment;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.12.03
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
	// TTL dos datagramas gerados.
	private int ttl;
	// Horário de envio dos datagramas; 
	private double sendTime;
	// Conta os pacotes ICMP recebidos
	private int icmpCount;
	// Interface que será utilizada para envio os datagramas.
	private HostInterface hostInterface;
	// Contador estático a ser utilizado na identificação das mensagens DNS.
	private static int count = 0;
	// PrintStream para saída do traceroute.
	private PrintStream ps;
	// Porta alta para onde os pacotes serão enviados. 
	public static final int DESTINATION_PORT = 33434;
	// Numero de datagramas que serão enviados na rajada.
	public static final int PROBE_DATAGRAMS = 3;
	
	public void setHostInterface(HostInterface hostIntercae) {
		this.hostInterface = hostIntercae;
	}
	
	public TracerouteProcess(Socket socket, String host, String clientName) {
		super(socket);
		this.host = host;
		this.clientName = clientName;
		isWaitingDns = true;
		ttl = 0;
		icmpCount = 0;
		setupPrintStream();
	}
	
	public TracerouteProcess(Socket socket, String clientName) {
		super(socket);
		this.host = socket.getDestinationIp().getAddress();
		this.socket.setDestinationPort(DESTINATION_PORT);
		this.clientName = clientName;
		isWaitingDns = false;
		ttl = 0;
		icmpCount = 0;
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
	
	public void sendProbes() {
		sendTime = Chronometer.getTime();
		ttl++;
		for (int i = 0; i < PROBE_DATAGRAMS; i++)
			hostInterface.send(next(), sendTime);
	}
	
	private Datagram next() {
		Integer sourcePort = socket.getSourcePort();
		Ip sourceIp = socket.getSourceIp();
		Integer destinationPort = socket.getDestinationPort();
		Ip destinationIp = socket.getDestinationIp();
		UdpSegment segment = new UdpSegment(sourcePort, destinationPort);
		Datagram next = new Datagram(segment, sourceIp, destinationIp);
		next.setTtl(ttl);
		return next;
	}
	
	public void icmpReceive(IcmpSegment segment, Ip ip) {
		double now = Chronometer.getTime();
		double rtt = 1000.0 * (now - this.sendTime);
		printOutput(ip, rtt);
		if (segment.getType() == 11 && segment.getCode() == 0)
			if (icmpCount == 3) {
				icmpCount = 0;
				sendProbes();
			}
	}

	private void printOutput(Ip ip, double rtt) {
		if (++icmpCount == 1) {
			if (ps != null) ps.printf("%2d %s %8.3f ms", ttl, ip, rtt);
		} 
		else {
			if (ps != null) ps.printf(" %8.3f ms", rtt);
		}
		if (icmpCount == 3) {
			if (ps != null) ps.println();
		}
	}
}
