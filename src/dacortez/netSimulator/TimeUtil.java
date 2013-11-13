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
	// Diz se a simulação deve considerar os tempos reais de processamento pelos hosts.
	public static boolean hostRealProcessingTime = true;
	
	public static void setStartTime(double time) {
		setTime = System.nanoTime();
		TimeUtil.time = time;
	}
	
	public static double getEndTime() {
		if (hostRealProcessingTime)
			return time + elapsedTime();
		return time;
	}
	
	public static double elapsedTime() {
		long now = System.nanoTime();
		return (now - setTime) / 1000000000.0;
	}
}
