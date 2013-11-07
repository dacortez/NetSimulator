package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

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
	// Coleção de classes registradas ao evento NetworkEvent (roteadores).
	private List<NetworkEvent> networkEventListeners;
	
	public Integer getPort() {
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
	
	public void addNetworkEventListener(NetworkEvent listener) {
		if (networkEventListeners == null)
			networkEventListeners = new ArrayList<NetworkEvent>();
		networkEventListeners.add(listener);
	}
	
	@Override
	public void linkEventHandler(Datagram data) {
		System.out.println("Interface do roteador recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO DATAGRAMA PARA ROTEADOR]\n");
		for (NetworkEvent listener: networkEventListeners)
			listener.networkEventHandler(data);
	}
	
	@Override
	public String toString() {
		return "(" + ip + ":" + port + ", " + queueSize + ") => " + link; 
	}
}
