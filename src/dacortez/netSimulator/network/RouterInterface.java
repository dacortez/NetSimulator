package dacortez.netSimulator.network;

import dacortez.netSimulator.Interface;

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
	}

	@Override
	public void networkEventHandler(Datagram data) {
		System.out.println("Interface do roteador " + ip + " recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO DATAGRAMA PARA O ROTEADOR]\n");
		router.route(data);
	}
	
	@Override
	public String toString() {
		return "(" + ip + ":" + port + ", fila: " + queueSize + ") => " + link; 
	}
}
