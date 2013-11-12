package dacortez.netSimulator.network;

import dacortez.netSimulator.Simulator;
import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.InRouter;
import dacortez.netSimulator.events.OutRouter;


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

	private int currentSize;
	
	private double queueEmptyTime;
	
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
		currentSize = 0;
		queueEmptyTime = 0.0;
	}

	@Override
	public void networkEventHandler(EventArgs args) {
		Simulator.addToQueue(new InRouter(this, args));
	}
	
	public void queueing(EventArgs args) {
		if (currentSize < queueSize) {
			Datagram data = args.getDatagram();
			double time = args.getTime();
			if (++currentSize == 1) queueEmptyTime = time;
			queueEmptyTime += router.getProcessingTime(data);
			EventArgs out = new EventArgs(data, queueEmptyTime);
			
			// DEBUG
			System.err.println("qs  " + ip + ": " + currentSize);
			System.err.println("qet " + ip + ": " + queueEmptyTime);
			System.err.println();
			
			Simulator.addToQueue(new OutRouter(this, out));
		}
	}
	
	public void route(EventArgs args) {
		currentSize--;
		router.route(args.getDatagram(), args.getTime());
	}
	
	@Override
	public String toString() {
		return "(" + ip + ":" + port + ", fila: " + queueSize + ") => " + link; 
	}
}
