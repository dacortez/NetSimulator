package dacortez.netSimulator;

import dacortez.netSimulator.application.HttpClient;
import dacortez.netSimulator.parser.Parser;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Simulator {
	// Objeto responsável pelo parser do arquivo de entrada da simulação.
	private Parser parser;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java -jar netSimulator.jar <arquivo_de_entrada>");
			return;
		}
		Simulator sim = new Simulator();
		sim.simulate(args[0]);
	}
	
	public void simulate(String file) {
		parser = new Parser(file);
		if (parser.parse())
			parser.printElements();
		HttpClient httpc1 = parser.getHttpClients().get("httpc1");
		httpc1.test();
		
		HttpClient httpc0 = parser.getHttpClients().get("httpc0");
		httpc0.test();
	}
}
