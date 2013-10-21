package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Ip {
	// Forma textual do endereço IP ("byte.byte.byte.byte").
	private String address; 
	// Os quatro bytes (0 a 255) do endereço IP. 
	private int[] bytes;
	
	public String getAddress() {
		return address;
	}
	
	public int[] getBytes() {
		return bytes;
	}
	
	public Ip(String address) {
		this.address = address;
		buildBytes();
	}
	
	private void buildBytes() {
		bytes = new int[4];
		int k = 0, j = 0;
		for (int i = 0; i < address.length(); i++)
			if (address.charAt(i) == '.') {
				bytes[k++] = Integer.parseInt(address.substring(j, i));
				j = i + 1;
			}
		bytes[k++] = Integer.parseInt(address.substring(j));
	}
	
	@Override
	public String toString() {
		return address;
	}
}
