package dacortez.netSimulator.application;

import dacortez.netSimulator.Simulator;

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
	
	public Message(String string) {
		data = string.getBytes();
	}
	
	public String dataToString() {
		StringBuilder sb = new StringBuilder();
		for (byte b: data)
			sb.append((char) b);
		return sb.toString();
	}
	
	public byte[] toBytes() {
		return data;
	}
	
	public int getNumberOfBytes() {
		if (data != null)
			return data.length;
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DATA (").append(getNumberOfBytes()).append(" bytes)");
		if (Simulator.printData)
			sb.append(":\n").append(data());
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
		sb.append("\n-----------------------------------------------------------------------------------------------\n");
		for (byte b: data)
			if ((b >= 32 && b <= 126) || b == 9 || b == 10 || b == 13)
				sb.append((char) b);
			else {
				sb.append("(binary data)\n");
				sb.append("-----------------------------------------------------------------------------------------------");
				return sb.toString();
			}
		sb.append("\n-----------------------------------------------------------------------------------------------");
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
