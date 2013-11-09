package dacortez.netSimulator.application.process;

import dacortez.netSimulator.application.Message;

public class DnsServerListening implements ApplicationState {

	@Override
	public Message request() {
		return null;
	}

	@Override
	public Message respond(Message message) {
		return new Message("Servidor DNS responde Oi!");
	}
}
