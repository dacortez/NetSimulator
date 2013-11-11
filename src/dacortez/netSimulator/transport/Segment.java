package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.message.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Segment {
	// Mensagem da camada de aplicação associada ao segmento.
	private Message message;
	// Porta de origem.
	private Integer sourcePort;
	// Porta de destino.
	private Integer destinationPort;
	
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
	
	@Override
	public String toString() {
		return sourcePort + " " + destinationPort + "\n" + message; 
	}
}
