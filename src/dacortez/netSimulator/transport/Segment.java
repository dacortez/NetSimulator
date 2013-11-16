package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public abstract class Segment {
	// Mensagem da camada de aplicação associada ao segmento.
	protected Message message;
	// Porta de origem.
	protected Integer sourcePort;
	// Porta de destino.
	protected Integer destinationPort;

	public Message getMessage() {
		return message;
	}
	
	public Integer getSourcePort() {
		return sourcePort;
	}
	
	public Integer getDestinationPort() {
		return destinationPort;
	}
	
	public Segment(Message message, Integer sourcePort, Integer destinationPort) {
		this.message = message;
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
	}
	
	public abstract int getNumberOfBytes();
}
