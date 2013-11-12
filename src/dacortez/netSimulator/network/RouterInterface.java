package dacortez.netSimulator.network;

import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.events.InRouter;
import dacortez.netSimulator.events.OutRouter;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
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
		System.out.println("Interface do roteador " + ip + " recebeu datagrama:");
		System.out.println(args.getDatagram());
		System.out.println("[ENVIANDO EVENTO IN_ROUTER PARA O SIM]\n");
		InRouter e = new InRouter(this, args);
		fireSimEvent(e);
	}
	
	public void queueing(EventArgs args) {
		System.out.println("Interface " + ip + " do roteador 'queueing' datagrama:");
		System.out.println(args.getDatagram());
		System.out.println("[ENVIANDO EVENTO OUT_ROUTER PARA O SIM]\n");
		if (currentSize < queueSize) {
			Datagram data = args.getDatagram();
			double time = args.getTime();
			currentSize++;
			if (currentSize == 1) queueEmptyTime = time;
			queueEmptyTime += router.getProcessingTime(data);
			EventArgs out = new EventArgs(data, queueEmptyTime);
			OutRouter e = new OutRouter(this, out);
			fireSimEvent(e);
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
