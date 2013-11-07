package dacortez.netSimulator.transport;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.ApplicationEvent;
import dacortez.netSimulator.application.Message;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public abstract class ServiceProvider implements TransportEvent {	
	// Interface do host associado.
	protected HostInterface hostInterface;
	// Coleção de classes registradas ao evento ApplicationEvent (hosts).
	private List<ApplicationEvent> applicationEventListeners;

	public ServiceProvider(HostInterface hostInterface) {
		this.hostInterface = hostInterface;
		hostInterface.addTransportEventListener(this);
	}

	public void addApplicationEventListener(ApplicationEvent listener) {
		if (applicationEventListeners == null)
			applicationEventListeners = new ArrayList<ApplicationEvent>();
		applicationEventListeners.add(listener);
	}
	
	public abstract void send(Message message, Integer destinationPort, Ip destinationIp);

	@Override
	public void transportEventHandler(Segment segment) {
		for (ApplicationEvent listener: applicationEventListeners)
			listener.applicationEventHandler(segment.getMessage());
	}
}
