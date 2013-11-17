package dacortez.netSimulator.network;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.transport.Protocol;
import dacortez.netSimulator.transport.Segment;
import dacortez.netSimulator.transport.TcpSegment;
import dacortez.netSimulator.transport.UdpSegment;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Datagram {
	// Tamanho do cabeçalho IP em bytes.
	private static final int HEADER_SIZE = 20;
	// Contador estático para identificação dos datagrmas.
	public static int count = 0;
	// Identificador do datagrama.
	private int id;
	// Segmento da camada de transporte associado ao datagrama.
	private Segment segment;
	// Endereço IP de origem.
	private Ip sourceIp;
	// Endereço IP de destino.
	private Ip destinationIp;
	// Time to live.
	private int ttl;
	// Upper layer protocol.
	private Protocol upperLayerProtocol;
	// Valor padrão do TTL.
	private static final int STANDARD_TTL = 64;
	
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
	
	public int getTtl() {
		return ttl;
	}
	
	public Protocol getUperLayerProtocol() {
		return upperLayerProtocol;
	}
	
	public Datagram(Segment segment, Ip sourceIp, Ip destinationIp) {
		id = ++count;
		this.segment = segment;
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
		ttl = STANDARD_TTL;
		setUpperLayersProtocol(segment);
	}

	private void setUpperLayersProtocol(Segment segment) {
		if (segment instanceof UdpSegment)
			upperLayerProtocol = Protocol.UDP;
		else if (segment instanceof TcpSegment)
			upperLayerProtocol = Protocol.TCP;
	}
	
	public void decrementTtl() {
		if (ttl > 0)
			ttl--;
	}
	
	public int getNumberOfBytes() {
		return HEADER_SIZE + segment.getNumberOfBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DATAGRAM #").append(id);
		sb.append(" (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append("Source IP = ").append(sourceIp).append("\n");
		sb.append("Destination IP = ").append(destinationIp).append("\n");
		sb.append("Upper layer protocol = ").append(upperLayerProtocol).append("\n");
		sb.append(segment);
		return sb.toString(); 
	}
}
