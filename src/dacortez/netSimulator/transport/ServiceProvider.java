package dacortez.netSimulator.transport;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;
import dacortez.netSimulator.network.HostInterface;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class ServiceProvider {	
	// Host associado a este provedor de serviços da camada de transporte.
	private Host host;
	// Interface do host associado.
	private HostInterface hostInterface;
	// Gerador de números aleatórios.
	private Random random;
	// Hash de controladores TCP (um para cada processo).
	private HashMap<Process, TcpController> controllers;
	// Valor mínimo da porta que pode ser escolhida ao vincular um socket.
	private static final int MIN_BIND_PORT = 4000;
	// Quantidade de portas que podem ser escolhidas ao vincular um socket.
	private static final int BIND_PORT_RANGE = 6000;
	
	
	public Host getHost() {
		return host;
	}
	
	public void setHost(Host host) {
		this.host = host;
	}
	
	public HostInterface getHostInterface() {
		return hostInterface;
	}
	
	public ServiceProvider() {
		hostInterface = new HostInterface(this);
		random = new Random();
		controllers = new HashMap<Process, TcpController>();
	}
	
	public void udpSend(Message message, Process process) {
		Segment segment = udpMultiplexing(message, process);
		Socket socket = process.getSocket();
		hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	// Multiplexing: gathering data at the source host from different application 
	// processes, enveloping the data with header information to create segments, 
	// and passing the segments to the network layer.	
	private UdpSegment udpMultiplexing(Message message, Process process) {
		bindProcessSocket(process);
		Socket socket = process.getSocket();
		return new UdpSegment(message, socket.getSourcePort(), socket.getDestinationPort());
	}

	public void bindProcessSocket(Process process) {
		Socket socket = process.getSocket();
		if (socket.getSourcePort() == null) {
			Integer sourcePort = MIN_BIND_PORT + random.nextInt(BIND_PORT_RANGE);
			socket.setSourceIp(hostInterface.getIp());
			socket.setSourcePort(sourcePort);
		}
	}
	
	public void tcpSend(Message message, Process process) {
		if (controllers.containsKey(process)) {
			controllers.get(process).send(message);
		}
		else {
			TcpController controller = new TcpController(process, hostInterface, TcpState.CLOSED);
			controllers.put(process, controller);
			controller.send(message);
		}
	}

	public void udpReceive(UdpSegment segment, Ip sourceIp, Ip destinationIp) {
		Process process = demultiplexing(segment, sourceIp, destinationIp);
		if (process != null)
			host.receive(segment.getMessage(), process);
		else
			System.out.println("Socket fechado!");
	}

	public void tcpReceive(TcpSegment segment, Ip sourceIp, Ip destinationIp) {
		Process process = demultiplexing(segment, sourceIp, destinationIp);
		if (process != null) {
			if (controllers.containsKey(process)) {
				controllers.get(process).receive(segment);
			}
			else {
				TcpController controller = new TcpController(process, hostInterface, TcpState.LISTEN);
				controllers.put(process, controller);
				controller.receive(segment);
			}
		}
		else 
			System.out.println("Socket fechado!");
	}
	
	public void timeout(TcpSegment segment, Ip sourceIp, Ip destinationIp) {
		//Process process = demultiplexing(segment, sourceIp, destinationIp);
		//if (process != null)
			//controllers.get(process).timeout(segment);
	}

	// Demultiplexing: delivering the data in a transport-layer segment 
	// to the correct application process. 
	private Process demultiplexing(Segment segment, Ip sourceIp, Ip destinationIp) {
		Process clientProcess = getClientProcess(segment);
		if (clientProcess != null) 
			return clientProcess;
		return forkOfServerProcess(segment, sourceIp, destinationIp);
	}
	
	private Process getClientProcess(Segment segment) { 
		List<Process> processes = host.getProcesses();
		for (Process process: processes) {
			Socket socket = process.getSocket();
			Integer sourcePort = segment.getSourcePort();
			Integer destinationPort = segment.getDestinationPort();
			if (socket.getSourcePort() == destinationPort && socket.getDestinationPort() == sourcePort)
				return process;
		}
		return null;
	}
	
	private Process forkOfServerProcess(Segment segment, Ip sourceIp, Ip destinationIp) {
		List<Process> processes = host.getProcesses();
		for (Process process: processes) {
			Socket socket = process.getSocket();
			if (socket.isServerSocket()) {
				Integer sourcePort = segment.getSourcePort();
				Integer destinationPort = segment.getDestinationPort();
				if (socket.getSourcePort() == destinationPort) {
					Process child = process.fork(); 
					child.getSocket().setDestinationIp(sourceIp);
					child.getSocket().setDestinationPort(sourcePort);
					host.addProcess(child);
					return child;
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "SERVICE_PROVIDER@" + hostInterface.toString();
	}
}
