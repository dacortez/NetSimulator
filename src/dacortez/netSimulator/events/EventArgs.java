package dacortez.netSimulator.events;

import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.11
 */
public class EventArgs {
	// Datagrama em transmissão associado ao evento.
	private Datagram data;
	// Instante de tempo da simulação quando ocorre o evento.
	private double time;
	
	public Datagram getDatagram() {
		return data;
	}
	
	public double getTime() {
		return time;
	}
	
	public EventArgs(double time) {
		data = null;
		this.time = time;
	}
	
	public EventArgs(Datagram data, double time) {
		this.data = data;
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Time: ").append(time).append("\n");
		if (data != null)
			sb.append(data);			
		return sb.toString();
	}
}
