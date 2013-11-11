package dacortez.netSimulator.events;

public class Finish extends SimEvent {

	public Finish(EventArgs args) {
		super(args);
	}

	@Override
	public void fire() {
		System.out.println("Fim da simulação!");
	}
	
	@Override
	public String toString() {
		return "*** Finish: " + args.toString();
	}
}
