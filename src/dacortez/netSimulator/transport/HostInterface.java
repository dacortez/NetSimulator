package dacortez.netSimulator.transport;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Interface;
import dacortez.netSimulator.network.Datagram;

public class HostInterface extends Interface {
	// Coleção de classes registradas ao evento TranportEvent (serviceProviders).
	private List<TransportEvent> transportEventListeners;

	public HostInterface() {
		// TODO Auto-generated constructor stub
	}
		
	public void addTransportEventListener(TransportEvent listener) {
		if (transportEventListeners == null)
			transportEventListeners = new ArrayList<TransportEvent>();
		transportEventListeners.add(listener);
	}
	
	@Override
	public void linkEventHandler(Datagram data) {
		System.out.println("Interface recebeu datagrama:");
		System.out.println(data);
		System.out.println("[REPASSANDO SEGMENTO PARA SERVICE_PROVIDER]\n");
		for (TransportEvent listener: transportEventListeners)
			listener.transportEventHandler(data.getSegment());
	}
}
