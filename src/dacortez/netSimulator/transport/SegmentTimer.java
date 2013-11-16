package dacortez.netSimulator.transport;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.18
 */
public class SegmentTimer {
	// Segmento TCP associado ao timer.
	private TcpSegment segment;
	// Timer associado ao segmento TCP.
	private Timer timer;
	// Referência ao controlador TCP dos segmentos.
	private TcpController controller;
	// Timeout a ser utilizado no timer (em ms).
	private static final long TIMEOUT = 500;
	
	public TcpSegment getSegment() {
		return segment;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public void setController(TcpController controller) {
		this.controller = controller;
	}

	public SegmentTimer(TcpSegment segment) {
		this.segment = segment;
		setTimer();
	}
	
	private void setTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				controller.timeout(segment);
			}
		}, 0,  TIMEOUT);
	}
}
