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
		Socket socket = process.getSocket();
		if (socket.getSourcePort() == null) {
			Integer sourcePort = 5000 + random.nextInt(5000);
			socket.setSourceIp(hostInterface.getIp());
			socket.setSourcePort(sourcePort);
		}
		return new UdpSegment(message, socket.getSourcePort(), socket.getDestinationPort());
	}
	
	public void tcpSend(Message message, Process process) {
		TcpController controller = new TcpController(process, hostInterface);
		controllers.put(process, controller);
		controller.establishConnection();
		controller.send(message);
	}

	public void receive(Segment segment, Ip sourceIp, Ip destinationIp) {
		if (segment instanceof UdpSegment) {
			Process process = udpDemultiplexing(segment, sourceIp, destinationIp);
			host.receive(segment.getMessage(), process);
		}
		else if (segment instanceof TcpSegment) {
			
		}
	}

	// Demultiplexing: delivering the data in a transport-layer segment 
	// to the correct application process. 
	private Process udpDemultiplexing(Segment segment, Ip sourceIp, Ip destinationIp) {
		List<Process> processes = host.getProcesses();
		for (Process process: processes) {
			Socket socket = process.getSocket();
			Integer sourcePort = segment.getSourcePort();
			Integer destinationPort = segment.getDestinationPort();
			if (socket.isServerSocket()) {
				if (socket.getSourcePort() == destinationPort) {
					socket.setDestinationIp(sourceIp);
					socket.setDestinationPort(sourcePort);
					return process;
				}
			}
			else if (socket.getSourcePort() == destinationPort && socket.getDestinationPort() == sourcePort)
				return process;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "SERVICE_PROVIDER@" + hostInterface.toString();
	}
}
