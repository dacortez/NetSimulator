package dacortez.netSimulator.application.dns;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.14
 */
public class DnsQuestion {
	// Valor do nome sendo consultado.
	private String name;
	// O tipo de questão sendo realizada.
	private RRType type;
	
	public String getName() {
		return name;
	}

	public RRType getType() {
		return type;
	}
	
	public DnsQuestion(String name, RRType type) {
		this.name = name;
		this.type = type;
	}
	
	public int getNumberOfBytes() {
		return toString().length();
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + type + ")";
	}
}
