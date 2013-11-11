package dacortez.netSimulator.application;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.messages.Message;
import dacortez.netSimulator.events.SimEventListener;
import dacortez.netSimulator.transport.ServiceProvider;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class Host {
	// Nome textual do hospedeiro.
	protected String name;
	// Endereço IP do roteador padrão associado.
	protected Ip standardRouterIp;
	// Endereço IP do servidor DNS associado.
	protected Ip dnsServerIp;
	// Provedor de serviços da camada de transporte associado.
	protected ServiceProvider serviceProvider;
	// Socket da aplicação rodando no host.
	protected Socket socket;
	// Estado em que a aplicação rodando no host se encontra.
	protected AppState state;
	// Coleção de classes registradas ao evento SimEventListener (o simulator).
	private List<SimEventListener> listeners;
		
	public String getName() {
		return name;
	}
	
	public Ip getStandardRouterIp() {
		return standardRouterIp;
	}

	public void setStandardRouterIp(Ip standardRouterIp) {
		this.standardRouterIp = standardRouterIp;
	}

	public Ip getDnsServerIp() {
		return dnsServerIp;
	}

	public void setDnsServerIp(Ip dnsServerIp) {
		this.dnsServerIp = dnsServerIp;
	}
	
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public Host() {
	}
		
	public Host(String name) {
		this.name = name;
		serviceProvider = new ServiceProvider();
	}
			
	public void addSimEventListener(SimEventListener listener) {
		if (listeners == null)
			listeners = new ArrayList<SimEventListener>();
		listeners.add(listener);
	}
	
	public Ip getIp() {
		return serviceProvider.getHostInterface().getIp();
	}
	
	public void attach(Host host) {
		name = host.getName();
		standardRouterIp = host.getStandardRouterIp();
		dnsServerIp = host.getDnsServerIp();
		serviceProvider = host.getServiceProvider();
		serviceProvider.setHost(this);
	}
	
	public void receive(Message message, Socket socket) {
		System.out.println(message);
		if (socket != null) {
			System.out.println("[PROCESSANDO]\n");
			processReceived(message);
		}
		else
			System.out.println("Socket fechado!");
	}
	
	protected void processReceived(Message message) {
		
	}
	
	@Override
	public String toString() {
		return name + ": (" + serviceProvider + ", " + standardRouterIp + ", " + dnsServerIp + ")";
	}
}
