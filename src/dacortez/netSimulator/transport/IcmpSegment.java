package dacortez.netSimulator.transport;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.12.01
 */
public class IcmpSegment extends Segment {
	// Tamanho do cabeçalho ICMP.
	private static final int HEADER_SIZE = 8;
	// Tipo da mensagem ICMP.
	private Integer type;
	// Código da mensagem ICMP.
	private Integer code;
	// Descrição da mensagem.
	private String description;
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public IcmpSegment(Integer sourcePort, Integer destinationPort) {
		super(sourcePort, destinationPort);
	}
	
	@Override
	public int getNumberOfBytes() {
		return HEADER_SIZE + description.length();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ICMP (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append("Source port = ").append(sourcePort).append("\n");
		sb.append("Destination port = ").append(destinationPort).append("\n");
		sb.append("Type = ").append(type).append("\n");
		sb.append("Code = ").append(code).append("\n");
		sb.append("Description = ").append(description);
		return sb.toString();
	}
}
