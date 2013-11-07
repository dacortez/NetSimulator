package dacortez.netSimulator.network;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.transport.Segment;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class Datagram {
	// Segmento da camada de transporte associado ao datagrama.
	private Segment segment;
	// Endereço IP de origem.
	private Ip sourceIp;
	// Endereço IP de destino.
	private Ip destinationIp;
	
	public Segment getSegment() {
		return segment;
	}
	
	public Ip getSourceIp() {
		return sourceIp;
	}
	
	public Ip getDestinationIp() {
		return destinationIp;
	}
	
	public Datagram(Segment segment, Ip sourceIp, Ip destinationIp) {
		this.segment = segment;
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
	}
	
	@Override
	public String toString() {
		return sourceIp + "\n" + destinationIp + "\n" + segment; 
	}
}
