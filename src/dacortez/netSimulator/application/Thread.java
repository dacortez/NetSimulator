package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class Thread {
	// Ip da origem.
	private Ip sourceIp;
	// Porta da origem.
	private Integer sourcePort;
	// Ip do destino.
	private Ip destinationIp;
	// Porta do destino.
	private Integer destinationPort;
	
	public Ip getSourceIp() {
		return sourceIp;
	}
	
	public Integer getSourcePort() {
		return sourcePort;
	}
		
	public Ip getDestinationIp() {
		return destinationIp;
	}
	
	public void setDestinationIp(Ip destinationIp) {
		this.destinationIp = destinationIp;
	}
	
	public Integer getDestinationPort() {
		return destinationPort;
	}
	
	public void setDestinationPort(Integer destinationPort) {
		this.destinationPort = destinationPort;
	}
	
	public Thread(Ip sourceIp, Integer sourcePort) {
		this.sourceIp = sourceIp;
		this.sourcePort = sourcePort;
	}
}
