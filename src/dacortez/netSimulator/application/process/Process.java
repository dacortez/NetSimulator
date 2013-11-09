package dacortez.netSimulator.application.process;

import dacortez.netSimulator.Ip;
import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Process {
	// Ip de origem do hospedeiro rodando o processo.
	private Ip sourceIp;
	// Porta de origem do hospedeiro rodando o processo.
	private Integer sourcePort;
	// Ip de destino para onde as mensagens serão enviadas.
	private Ip destinationIp;
	// Porta de dentino que receberá as mensagens enviadas.
	private Integer destinationPort;
	// Estado em que o processo se encontra (determina as mensagens a serem trocadas). 
	private State state;
	
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

	public void setState(State state) {
		this.state = state;
	}
	
	public Process(State state) {
		this.state = state;
	}
	
	public Message request() {
		return state.request();
	}
	
	public Message respond(Message message) {
		return state.respond(message);
	}
}
