package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public abstract class SimEvent {
	// Interface responsável por disparar o evento.
	protected Interface sender;
	// Argumentos associado ao evento (datagram e instante da simulação).
	protected EventArgs args;
	
	public Interface getSender() {
		return sender;
	}
	
	public EventArgs getEventArgs() {
		return args;
	}
	
	public SimEvent(Interface sender, EventArgs args) {
		this.sender = sender;
		this.args = args;
	}
	
	// Melhorar este nome.
	public abstract void fire();
}
