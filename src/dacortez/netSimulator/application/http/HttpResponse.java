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
	public byte[] toBytes() {
		byte[] bytes = new byte[getNumberOfBytes()];
		byte[] headerBytes = header().getBytes();
		for (int i = 0; i < headerBytes.length; i++)
			bytes[i] = headerBytes[i];
		if (data != null)
			for (int i = 0; i < data.length; i++)
				bytes[headerBytes.length + i] = data[i];
		return bytes;
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
		sb.append("HTTP_RESPONSE (").append(getNumberOfBytes()).append(" bytes):\n");
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
			return binaryData();
		else if (contentType == HttpAccept.HTML)
			return textData();
		return null;
	}
}
