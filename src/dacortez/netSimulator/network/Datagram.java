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
	// Tamanho do cabeçalho IP em bytes.
	private static final int HEADER_SIZE = 20;
	
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
	
	public int getNumberOfBytes() {
		return HEADER_SIZE + segment.getNumberOfBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DATAGRAM #").append(id);
		sb.append(" (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append(sourceIp).append("\n");
		sb.append(destinationIp).append("\n");
		sb.append(segment).append("\n");
		return sb.toString(); 
	}
}
