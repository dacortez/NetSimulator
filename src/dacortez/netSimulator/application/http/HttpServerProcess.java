package dacortez.netSimulator.application.http;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

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
	public Message request() {
		return null;
	}

	@Override
	public Message respond(Message message) {
		HttpRequest request = (HttpRequest) message;
		try {
			return tryToRespond(request);
		} 
		catch (Exception e) {
			return badRequest(request);
		}
	}

	private Message tryToRespond(HttpRequest request) throws Exception {
		String resource = request.getResource();
		File file = new File(resource);
		if (file.exists()) {
			if (contentType(resource) == null) 
				return badRequest(request);
			else 
				return ok(request);
		}
		return notFound(request);
	}

	private Message ok(HttpRequest request) throws Exception {
		String resource = request.getResource();
		File file = new File(resource);
		HttpResponse ok = new HttpResponse(HttpStatus.OK);
		ok.setVersion(request.getVersion());
		ok.setConnection(request.getConnection());
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
		notFound.setVersion(request.getVersion());
		notFound.setConnection(request.getConnection());
		notFound.setServer(serverName);
		notFound.setLastModified(new Date(System.currentTimeMillis()));
		notFound.setData(notFoundHtml(resource));
		notFound.setContentType(HttpAccept.HTML);
		return notFound;
	}
	
	private HttpResponse badRequest(HttpRequest request) {
		String resource = request.getResource();
		HttpResponse badRequest = new HttpResponse(HttpStatus.BAD_REQUEST);
		badRequest.setVersion(request.getVersion());
		badRequest.setConnection(request.getConnection());
		badRequest.setServer(serverName);
		badRequest.setLastModified(new Date(System.currentTimeMillis()));
		badRequest.setData(badRequestHtml(resource));
		badRequest.setContentType(HttpAccept.HTML);
		return badRequest;
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
	
	private byte[] badRequestHtml(String resource) {
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
