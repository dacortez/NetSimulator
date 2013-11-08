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
	
	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public DuplexLink(double capacity, double delay) {
		this.capacity = capacity;
		this.delay = delay;
	}
	
	@Override
	public String toString() {
		return "[" + capacity + "Mbps, " + delay + "ms]";
	}
}
