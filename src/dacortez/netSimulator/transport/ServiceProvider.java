package dacortez.netSimulator.transport;

import java.util.Random;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Host;
import dacortez.netSimulator.application.Socket;
import dacortez.netSimulator.application.message.Message;
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
	
	private Random random;
	
	public void setHost(Host host) {
		this.host = host;
	}
	
	public HostInterface getHostInterface() {
		return hostInterface;
	}
	
	public ServiceProvider() {
		hostInterface = new HostInterface(this);
		random = new Random();
	}
	
	public void send(Message message, Socket socket) {
		System.out.println("Provider de serviços do host " + hostInterface.getIp() + " recebeu mensagem:");
		System.out.println(message);
		System.out.println("[MULTIPLEXING E PASSANDO SEGMENTO PARA INTERFACE DO HOST]\n");
		Segment segment = multiplexing(message, socket);
		hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
	}
	
	// Multiplexing: gathering data at the source host from different application 
	// processes, enveloping the data with header information to create segments, 
	// and passing the segments to the network layer.	
	private Segment multiplexing(Message message, Socket socket) {
		if (socket.getSourcePort() == null) {
			Integer sourcePort = 5000 + random.nextInt(5000);
			socket.setSourceIp(hostInterface.getIp());
			socket.setSourcePort(sourcePort);
		}
		return new Segment(message, socket.getSourcePort(), socket.getDestinationPort());
	}

	public void receive(Segment segment, Ip sourceIp, Ip destinationIp) {
		System.out.println("Provider de serviços do host " + hostInterface.getIp() + " recebeu segmento:");
		System.out.println(segment);
		System.out.println("[DEMULTIPLEXING E PASSANDO MENSSAGEM PARA A APLICAÇÃO]\n");
		Socket socket = demultiplexing(segment, sourceIp, destinationIp);
		host.receive(segment.getMessage(), socket);	
	}

	// Demultiplexing: delivering the data in a transport-layer segment 
	// to the correct application process. 
	private Socket demultiplexing(Segment segment, Ip sourceIp, Ip destinationIp) {
		Socket socket = host.getSocket();
		Integer sourcePort = segment.getSourcePort();
		Integer destinationPort = segment.getDestinationPort();
		if (socket == null) return null;
		if (socket.isListening()) {
			if (socket.getSourcePort() == destinationPort) {
				socket.setDestinationIp(sourceIp);
				socket.setDestinationPort(sourcePort);
				return socket;
			}
		}
		else if (socket.getSourcePort() == destinationPort && socket.getDestinationPort() == sourcePort)
			return socket;
		return null;
	}
	
	@Override
	public String toString() {
		return hostInterface.toString();
	}
}
