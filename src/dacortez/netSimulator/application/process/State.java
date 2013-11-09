package dacortez.netSimulator.application.process;

import dacortez.netSimulator.application.Message;

public interface State {
	Message request();
	Message respond(Message message);
}
