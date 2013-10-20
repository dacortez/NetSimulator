package dacortez.netSimulator.network;

import dacortez.netSimulator.transport.Segment;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Datagram {
	// Segmento da camada de transporte associado ao datagrama.
	private Segment segment;
	
	public Segment getSegment() {
		return segment;
	}
	
	public Datagram(Segment segment) {
		this.segment = segment;
	}
}
