package dacortez.netSimulator.application.http;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.application.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public class HttpRequest extends Message {
	// Método HTTP da requesição.
	private HttpMethod method;
	// Recurso procurado no servidor.
	private String resource;
	// Versão empregada pelo protocolo.
	private HttpVersion version = HttpVersion.HTTP_10;
	// Nome do servidor HTTP.
	private String host;
	// Tipo de conexão empregada.
	private HttpConnection connection = HttpConnection.CLOSE;
	// Tipo de cliente HTTP.
	private String userAgent = null;
	// Lista de arquivos aceitos como resposta.
	private List<HttpAccept> accepts;
	
	public String getResource() {
		return resource;
	}
	
	public HttpVersion getVersion() {
		return version;
	}
	
	public void setVersion(HttpVersion version) {
		this.version = version;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public HttpConnection getConnection() {
		return connection;
	}
	
	public void setConnection(HttpConnection connection) {
		this.connection = connection;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public HttpRequest(HttpMethod method, String resource) {
		this.method = method;
		this.resource = resource;
		accepts = new ArrayList<HttpAccept>();
	}
	
	public void addHttpAccept(HttpAccept accept) {
		accepts.add(accept);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder() ;
		sb.append(method).append(" ").append(resource).append(" ").append(version).append("\r\n");
		sb.append("Host: ").append(host).append("\r\n");
		sb.append("Connection: ").append(connection).append("\r\n");
		if (userAgent != null)
			sb.append("User-agent: ").append(userAgent).append("\r\n");
		sb.append("Accept: ");
		for (int i = 0; i < accepts.size(); i++) {
			HttpAccept accept = accepts.get(i);
			if (i == accepts.size() - 1)
				sb.append(accept).append("\r\n");
			else
				sb.append(accept).append(", ");
		}
		sb.append("\r\n");
		return sb.toString();
	}
}
