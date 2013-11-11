package dacortez.netSimulator;

import dacortez.netSimulator.parser.Parser;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public class Simulator {
	// Objeto responsável pela leitura do arquivo de entrada 
	// e construção dos objetos da simulação (a rede).
	private Parser parser;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java -jar netSimulator.jar <arquivo_de_entrada.ns>");
			return;
		}
		Simulator sim = new Simulator();
		sim.simulate(args[0]);
	}
	
	public void simulate(String file) {
		parser = new Parser(file);
		if (parser.parse()) {
			parser.printElements();
			parser.getDnsServer("dns3").start();
			parser.getHttpClient("httpc1").get("h2");
		}
	}
}
