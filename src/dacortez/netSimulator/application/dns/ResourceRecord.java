package dacortez.netSimulator.application.dns;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.14
 */
public class ResourceRecord {
	// Valor do nome sendo consultado.
	private String name;
	// O valor resultado da consulta
	private String value;
	// O tipo de questão sendo realizada.
	private RRType type;
	// Time to live em cache.
	private Integer ttl;
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public RRType getType() {
		return type;
	}
	
	public Integer getTtl() {
		return ttl;
	}
	
	public ResourceRecord(String name, String value, RRType type, Integer ttl) {
		this.name = name;
		this.value = value;
		this.type = type;
		this.ttl = ttl;
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + value + ", " + type + ", " + ttl + ")"; 
	}
}
