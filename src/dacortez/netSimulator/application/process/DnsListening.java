package dacortez.netSimulator.application.process;

import dacortez.netSimulator.application.Message;

public class DnsListening implements State {

	@Override
	public Message request() {
		return null;
	}

	@Override
	public Message respond(Message message) {
		return new Message("Servidor DNS responde Oi!");
	}
}
