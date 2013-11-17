package dacortez.netSimulator.application.http;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.StringTokenizer;

import com.sun.tools.javac.util.Convert;

import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public class HttpServerProcess extends Process {
	// Nome do host rodando o processo servidor HTTP.
	private String serverName;
	
	public HttpServerProcess(Socket socket, String serverName) {
		super(socket);
		this.serverName = serverName;
	}
	
	@Override
	public Process fork() {
		return new HttpServerProcess(socket.clone(), serverName);
	}
	
	@Override
	public Message request() {
		return null;
	}

	@Override
	public Message respond(Message message) {
		try {
			return tryToRespond(message);
		} 
		catch (Exception e) {
			return internal();
		}
	}

	private Message tryToRespond(Message message) throws Exception {
		HttpRequest request = parse(message);
		if (request == null) return badRequest(); 
		String resource = request.getResource();
		File file = new File(resource);
		if (file.exists()) {
			if (contentType(resource) == null) 
				return badRequest();
			else 
				return ok(request);
		}
		return notFound(request);
	}
	
	private HttpRequest parse(Message message) {
		String str = Convert.utf2string(message.toBytes());
		StringTokenizer tokenizer = new StringTokenizer(str);	
		if (tokenizer.nextToken().contentEquals("GET")) {
			String resource = tokenizer.nextToken();
			return new HttpRequest(HttpMethod.GET, resource); 
		}
		return null;
	}

	private Message ok(HttpRequest request) throws Exception {
		String resource = request.getResource();
		File file = new File(resource);
		HttpResponse ok = new HttpResponse(HttpStatus.OK);
		ok.setServer(serverName);
		ok.setLastModified(new Date(file.lastModified()));
		ok.setData(getData(file));
		ok.setContentType(contentType(resource));
		return ok;
	}
	
	private byte[] getData(File file) throws Exception {
		DataInputStream stream = new DataInputStream(new FileInputStream(file));
		byte data[] = new byte[(int) file.length()];
		stream.read(data);
		stream.close();
		return data;
	}
	
	private HttpResponse notFound(HttpRequest request) {
		String resource = request.getResource();
		HttpResponse notFound = new HttpResponse(HttpStatus.NOT_FOUND);
		notFound.setServer(serverName);
		notFound.setLastModified(new Date(System.currentTimeMillis()));
		notFound.setData(notFoundHtml(resource));
		notFound.setContentType(HttpAccept.HTML);
		return notFound;
	}
	
	private HttpResponse badRequest() {
		HttpResponse badRequest = new HttpResponse(HttpStatus.BAD_REQUEST);
		badRequest.setServer(serverName);
		badRequest.setLastModified(new Date(System.currentTimeMillis()));
		badRequest.setData(badRequestHtml());
		badRequest.setContentType(HttpAccept.HTML);
		return badRequest;
	}
	
	private HttpResponse internal() {
		HttpResponse internal = new HttpResponse(HttpStatus.INTERNAL);
		internal.setServer(serverName);
		internal.setLastModified(new Date(System.currentTimeMillis()));
		internal.setData(internalHtml());
		internal.setContentType(HttpAccept.HTML);
		return internal;
	}
	
	private byte[] notFoundHtml(String resource) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n");
		sb.append("<html><head>\n");
		sb.append("<title>404 Not Found</title>\n");
		sb.append("</head><body>\n");
		sb.append("<h1>Not Found</h1>\n");
		sb.append("<p>The requested URL /").append(resource);
		sb.append(" was not found on this server.</p>\n");
		sb.append("</body></html>");
		return sb.toString().getBytes();
	}
	
	private byte[] badRequestHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n");
		sb.append("<html><head>\n");
		sb.append("<title>400 Bad Request</title>\n");
		sb.append("</head><body>\n");
		sb.append("<h1>Not Found</h1>\n");
		sb.append("<p>Your browser sent a request that this server could not understand.<p>\n");
		sb.append(" was not found on this server.</p>\n");
		sb.append("</body></html>");
		return sb.toString().getBytes();
	}
	
	private byte[] internalHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n");
		sb.append("<html><head>\n");
		sb.append("<title>500 Internal Server Error</title>\n");
		sb.append("</head><body>\n");
		sb.append("<h1>Internal Server Error</h1>\n");
		sb.append("</body></html>");
		return sb.toString().getBytes();
	}
	
	private HttpAccept contentType(String resource) {
		int lastPoint = resource.lastIndexOf(".");
		String extension = resource.substring(lastPoint + 1).trim().toLowerCase();
		if (extension.contentEquals("html"))
			return HttpAccept.HTML;
		if (extension.contentEquals("png"))
			return HttpAccept.PNG;
		return null;
	}
}
