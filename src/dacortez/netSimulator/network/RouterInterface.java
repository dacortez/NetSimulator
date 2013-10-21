package dacortez.netSimulator.network;

import dacortez.netSimulator.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class RouterInterface extends Interface {
	// O número da porta da inteface do roteador.
	private Integer port;
	// Tamanho da fila em quantidade de pacotes.
	private int queueSize;
	
	public int getPort() {
		return port;
	}
	
	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
		
	public RouterInterface(Integer port) {
		super();
		this.port = port;
	}
	
	@Override
	public String toString() {
		return "(" + ip.toString() + ":" + port.toString() + ", " + queueSize + ")"; 
	}
}
