package dacortez.netSimulator.application;

import dacortez.netSimulator.Ip;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Socket {
	// Ip de origem do hospedeiro rodando o processo.
	private Ip sourceIp;
	// Porta de origem do hospedeiro rodando o processo.
	private Integer sourcePort;
	// Ip de destino para onde as mensagens serão enviadas.
	private Ip destinationIp;
	// Porta de dentino que receberá as mensagens enviadas.
	private Integer destinationPort;
	
	public Ip getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(Ip sourceIp) {
		this.sourceIp = sourceIp;
	}

	public Integer getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
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
	
	public Socket() {
		sourceIp = destinationIp = null;
		sourcePort = destinationPort = null;
	}
	
	public boolean isServerSocket() {
		return (destinationIp == null && destinationPort == null);
	}
	
	@Override
	public Socket clone() {
		Socket clone = new Socket();
		clone.setSourceIp(sourceIp);
		clone.setSourcePort(sourcePort);
		clone.setDestinationIp(destinationIp);
		clone.setDestinationPort(destinationPort);
		return clone;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("SOCKET:\n");
		sb.append("<= Source ").append(sourceIp).append(":").append(sourcePort).append("\n");
		sb.append("=> Destination ").append(destinationIp).append(":").append(destinationPort).append("\n");
		return sb.toString();
	}
}
