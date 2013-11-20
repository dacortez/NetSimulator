package dacortez.netSimulator.network;

import java.util.ArrayList;

import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.12
 */
public class RouterInterface extends Interface {
	// Roteador associado a esta interface.
	private Router router;
	// O número da porta da inteface do roteador.
	private Integer port;
	// Tamanho da fila em quantidade de pacotes.
	private int queueSize;
	
	public Integer getPort() {
		return port;
	}
	
	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
			
	public RouterInterface(Router router, Integer port) {
		super();
		this.router = router;
		this.port = port;
		queue = new ArrayList<EventArgs>();
	}
	
	@Override
	public void queuing(EventArgs args) {
		if (queue.size() < queueSize)
			super.queuing(args);
		else {
			if (Simulator.debugMode) {
				System.out.println("--- Pacote perdido em " + ip + ":" + port + ":\n");
				System.out.println("DATAGRAM #" + args.getDatagram().getId());
			}
		}
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		Datagram data = args.getDatagram();
		double time = args.getTime();
		router.route(data, time);
	}
		
	@Override
	public String toString() {
		return "(" + ip + ":" + port + ", queue_size: " + queueSize + ") => " + link; 
	}
}
