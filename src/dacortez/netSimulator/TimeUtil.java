package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public class TimeUtil {
	// Armazena instante de tempo da simulação.
	private static double time;
	// Aramazena o instante de tempo real em que o método setStartTime() foi chamado.
	private static long setTime;
	
	public static void setStartTime(double time) {
		TimeUtil.time = time;
		setTime = System.currentTimeMillis();
	}
	
	public static double getEndTime() {
		long now = System.currentTimeMillis();
		return time + (now - setTime) / 1000.0;
	}
}
