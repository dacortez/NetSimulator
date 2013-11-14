package dacortez.netSimulator.application.messages;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Message {
	// Para fins de testes.
	private String data;
	
	public String getData() {
		return data;
	}
	
	public Message() {
		this.data = null;
	}
	
	public Message(String data) {
		this.data = data;
	}
	
	public int getNumberOfBytes() {
		return data.length();
	}
	
	@Override
	public String toString() {
		return data;
	}
}
