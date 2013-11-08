package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.06
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
	
	public Ip subNetIp() { 
		int k = address.lastIndexOf('.');
		String subNet = address.substring(0, k) + ".0";
		return new Ip(subNet);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Ip))
			return false;
		Ip other = (Ip) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.contentEquals(other.address))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return address;
	}
}
