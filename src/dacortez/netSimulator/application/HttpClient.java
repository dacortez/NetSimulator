package dacortez.netSimulator.application;

import dacortez.netSimulator.transport.TcpProvider;
import dacortez.netSimulator.transport.UdpProvider;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class HttpClient extends Host implements ApplicationEvent {
	// Nome do cliente HTTP.
	private String clientName;
	// Provedor de serviços UDP (estaria no kernel do "SO").
	private UdpProvider udpProvider;
	// Provedor de serviços TCP (estaria no kernel do "SO").
	private TcpProvider tcpProvider;
	
	public String getClientName() {
		return clientName;
	}

	public HttpClient(String clientName) {
		super();
		this.clientName = clientName;
	}
	
	@Override
	public void attach(Host host) {
		super.attach(host);
		udpProvider = new UdpProvider(hostInterface);
		tcpProvider = new TcpProvider(hostInterface);
		udpProvider.addApplicationEventListener(this);
		tcpProvider.addApplicationEventListener(this);
	}
	
	// Método de teste preliminar.
	public void test() {
		Message message = new Message("Oi, mundo!");
		udpProvider.send(message, 53, dnsServerIp);
	}
	
	@Override
	public void applicationEventHandler(Message message) {
		// TODO
	}
	
	@Override
	public String toString() {
		return clientName + " <- " + super.toString();
	}
}
