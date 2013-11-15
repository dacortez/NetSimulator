package dacortez.netSimulator.application.http;

import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public class HttpServerProcess extends Process {

	public HttpServerProcess(Socket socket) {
		super(socket);
	}
	
	@Override
	public Message request() {
		return null;
	}

	@Override
	public Message respond(Message message) {
		return new HttpResponse("HTTP/1.0 200 OK");
	}
}
