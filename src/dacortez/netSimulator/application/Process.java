package dacortez.netSimulator.application;

public class Process {
	// Socket associado ao processo rodando na aplicação.
	private Socket socket;
	// Estado em que o processo se encontra.
	private ProcessState state;
	
	public Socket getSocket() {
		return socket;
	}

	public ProcessState getState() {
		return state;
	}

	public Process(Socket socket, ProcessState state) {
		this.socket = socket;
		this.state = state;
	}
}
