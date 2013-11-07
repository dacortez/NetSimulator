package dacortez.netSimulator.application;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public class Message {
	// Para fins de testes.
	private String data;
	
	public String getData() {
		return data;
	}
	
	public Message(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data;
	}
}
