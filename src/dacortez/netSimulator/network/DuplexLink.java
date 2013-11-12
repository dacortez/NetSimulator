package dacortez.netSimulator.network;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public class DuplexLink {
	// Capacidade do enlace em Mbps.
	private double capacity;
	// Atraso da transmissão em ms.
	private double delay;
	
	public DuplexLink(double capacity, double delay) {
		this.capacity = capacity;
		this.delay = delay;
	}
	
	public double getTransmissionTime(int numberOfbytes) {
		return (delay / 1000.0) +  (8.0 * numberOfbytes) / (1000000.0 * capacity); 
	}
	
	@Override
	public String toString() {
		return "[" + capacity + "Mbps, " + delay + "ms]";
	}
}
