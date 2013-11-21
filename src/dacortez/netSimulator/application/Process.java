package dacortez.netSimulator.application;


/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.15
 */
public abstract class Process {
	// Número identificador do processo.
	private int pid;
	// Contador estático para ser utilizado na identificação do processo.
	private static int count = 0;
	// Socket associado ao processo rodando na aplicação.
	protected Socket socket;
	
	public int getPid() {
		return pid;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public Process(Socket socket) {
		this.socket = socket;
		pid = ++count;
	}
	
	public abstract Process fork();
	
	public abstract Message request();
	
	public abstract Message respond(Message message);
	
	@Override
	public String toString() {
		return "PROCESS #" + pid + "\n" + socket.toString();
	}
}
