package dacortez.netSimulator.application.process;

import dacortez.netSimulator.application.Message;

public class DnsLooking implements State {

	@Override
	public Message request() {
		return new Message("Oi, DNS Server!");
	}

	@Override
	public Message respond(Message message) {
		return null;
	}
}
