package dacortez.netSimulator.application.http;

import java.util.Date;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public class HttpResponse extends Message {
	// Versão empregada pelo protocolo.
	private HttpVersion version;
	// Código de status da resposta e frase correspondente.
	private HttpStatus status;
	// Tipo de conexão empregada.
	private HttpConnection connection;
	// Data da resposta.
	private Date date;
	// Nome do processo servidor HTTP.
	private String server;
	// Data de última modificação do recurso.
	private Date lastModified;
	// Número de bytes do recurso.
	private int contentLength;
	// Tipo de recurso.
	private HttpAccept contentType;
	// Dados do recurso.
	private byte[] data;
	// Vetor utilizado para conversão de bytes em hexadecimal.
	private static final char[] chars = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'a', 'b', 'c', 'd', 'd', 'e', 'f'
	};
	
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	public void setVersion(HttpVersion version) {
		this.version = version;
	}
	
	public void setConnection(HttpConnection connection) {
		this.connection = connection;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public void setContentType(HttpAccept contentType) {
		this.contentType = contentType;
	}
	
	public void setData(byte[] data) {
		this.data = data;
		contentLength = data.length;
	}
	
	public HttpResponse(HttpStatus status) {
		this.status = status;
		date = new Date(System.currentTimeMillis());
	}
	
	@Override
	public int getNumberOfBytes() {
		if (data == null)
			return header().length();
		return header().length() + data.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(header());
		sb.append(data());
		return sb.toString();
	}
	
	private String header() {
		StringBuilder sb = new StringBuilder();
		sb.append(version).append(" ").append(status).append("\r\n");
		sb.append("Connection: ").append(connection).append("\r\n");
		sb.append("Date: ").append(date.toString()).append("\r\n");
		sb.append("Server: ").append(server).append("\r\n");
		sb.append("Last-Modified: ").append(lastModified).append("\r\n");
		sb.append("Content-Length: ").append(contentLength).append("\r\n");
		sb.append("Content-Type: ").append(contentType).append("\r\n");
		sb.append("\r\n");
		return sb.toString();
	}
	
	private String data() {
		if (contentType == HttpAccept.PNG)
			return pngData();
		else if (contentType == HttpAccept.HTML)
			return htmlData();
		return null;
	}

	private String pngData() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(toHex(data[i])).append(" ");
			if ((i + 1) % 16 == 0)
				sb.append("\n");
		}
		return sb.toString();
	}
	
	private String htmlData() {
		StringBuilder sb = new StringBuilder();
		for (byte b: data)
			sb.append((char) b);
		return sb.toString();
	}
	
	private String toHex(byte b) {
		StringBuilder sb = new StringBuilder();
		if (b < 0) b += 128;
		int first = b / 16;
		int second = b % 16;
		sb.append(chars[first]).append(chars[second]);
		return sb.toString();
	}
}
