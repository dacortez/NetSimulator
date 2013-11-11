package dacortez.netSimulator.network;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.transport.Segment;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Datagram {
	// Contador estático para identificação dos datagrmas.
	private static int count = 0;
	// Identificador do datagrama.
	private int id;
	// Segmento da camada de transporte associado ao datagrama.
	private Segment segment;
	// Endereço IP de origem.
	private Ip sourceIp;
	// Endereço IP de destino.
	private Ip destinationIp;
	
	public int getId() {
		return id;
	}
	
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
		id = ++count;
		this.segment = segment;
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
	}
	
	@Override
	public String toString() {
		return sourceIp + "\n" + destinationIp + "\n" + segment; 
	}
}
