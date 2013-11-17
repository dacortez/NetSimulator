package dacortez.netSimulator.application.dns;

import java.util.List;

import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.08
 */
public class DnsServerProcess extends Process {
	// Mapa de endereços IPs.
	private List<ResourceRecord> resourceRecords;
		
	public DnsServerProcess(Socket socket, List<ResourceRecord> resourceRecords) {
		super(socket);
		this.resourceRecords = resourceRecords;
	}
	
	@Override
	public Process fork() {
		return new DnsServerProcess(socket.clone(), resourceRecords);
	}
	
	@Override
	public Message request() {
		return null;
	}

	@Override
	public Message respond(Message message) {
		DnsMessage request = (DnsMessage) message;
		DnsMessage respond = null;
		if (!request.isReply()) {
			respond = new DnsMessage(request.getId(), true);
			for (DnsQuestion question: request.getQuestions()) {
				DnsAnswer answer = answer(question);
				if (answer != null) respond.addAnswer(answer);
			}
		}
		return respond;
	}

	private DnsAnswer answer(DnsQuestion question) {
		for (ResourceRecord rr: resourceRecords)
			if (question.getName().contentEquals(rr.getName())) 
				return new DnsAnswer(rr.getType(), rr.getValue(), rr.getTtl()); 
		return null;
	}
}
