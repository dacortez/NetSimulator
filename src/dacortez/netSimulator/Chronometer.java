package dacortez.netSimulator;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public class Chronometer {
	// Armazena instante de tempo da simulação.
	private static double time;
	// Aramazena o instante de tempo real em que o método setStartTime() foi chamado.
	private static long startTime;
	// Diz se a simulação deve considerar os tempos reais de processamento pelos hosts.
	public static boolean useRealProcessingTime = true;
	
	public static void setTime(double time) {
		startTime = System.nanoTime();
		Chronometer.time = time;
	}
	
	public static double getTime() {
		double endTime = time;
		if (useRealProcessingTime)
			endTime = time + elapsedTime();
		setTime(endTime);
		return endTime;
	}
	
	public static double elapsedTime() {
		long now = System.nanoTime();
		return (now - startTime) / 1000000000.0;
	}
}
