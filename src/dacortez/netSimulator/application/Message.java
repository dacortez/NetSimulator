package dacortez.netSimulator.application;

import com.sun.tools.javac.util.Convert;



/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.16
 */
public class Message {
	// Vetor de bytes representando os dados carregados na mensagem.
	protected byte[] data;
	// Vetor utilizado para conversão de bytes em hexadecimal.
	private static final char[] hexChars = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'a', 'b', 'c', 'd', 'd', 'e', 'f'
	};
	
	public Message() {
		data = null;
	}
	
	public Message(byte[] data) {
		this.data = data;
	}
	
	public Message(int size) {
		data = new byte[size];
	}
	
	public byte[] toBytes() {
		return data;
	}
	
	public int getNumberOfBytes() {
		return data.length;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DATA (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append(data());
		return sb.toString();
	}

	protected String data() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(toHex(data[i])).append(" ");
			if ((i + 1) % 32 == 0)
				sb.append("\n");
		}
		if (sb.charAt(sb.length() - 1) == '\n')
			sb.deleteCharAt(sb.length() - 1);
		sb.append(dataToText());
		return sb.toString();
	}

	private String dataToText() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n===============================================================================================\n");
		for (byte b: data)
			if (b < 9) {
				sb.append("(binary data)");
				sb.append("\n===============================================================================================");
				return sb.toString();
			}
		sb.append(Convert.utf2string(data));
		sb.append("\n===============================================================================================");
		return sb.toString();
	}
	
	private String toHex(byte b) {
		StringBuilder sb = new StringBuilder();
		if (b < 0) b += 128;
		int first = b / 16;
		int second = b % 16;
		sb.append(hexChars[first]).append(hexChars[second]);
		return sb.toString();
	}
}
