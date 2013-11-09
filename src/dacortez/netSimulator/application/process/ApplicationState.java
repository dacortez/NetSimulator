package dacortez.netSimulator.application.process;

import dacortez.netSimulator.application.Message;

public interface ApplicationState {
	Message request();
	Message respond(Message message);
}
