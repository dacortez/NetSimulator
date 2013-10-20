package dacortez.netSimulator.transport;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Segment {
	// Mensagem da camada de aplicação associada ao segmento.
	private Message message;
	
	public Message getMessage() {
		return message;
	}
	
	public Segment(Message message) {
		this.message = message;
	}
}
