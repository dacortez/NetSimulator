package dacortez.netSimulator.application.dns;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.14
 */
public class DnsAnswer {
	// O tipo de questão sendo realizada.
	private RRType type;
	// O valor resultado da consulta
	private String value;
	// Time to live em cache.
	private Integer ttl;
	
	public RRType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public int getTtl() {
		return ttl;
	}
			
	public DnsAnswer(RRType type, String value, Integer ttl) {
		this.type = type;
		this.value = value;
		this.ttl = ttl;
	}
	
	public int getNumberOfBytes() {
		return toString().length();
	}
	
	@Override
	public String toString() {
		return "(" + type + ", " + value + ", " + ttl + ")";
	}
}
