package dacortez.netSimulator;

import java.io.File;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Simulator {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java -jar netSimulator.jar <arquivo_de_entrada>");
			return;
		}
		File input = new File(args[0]);
		if (!input.exists()) {
			System.out.println("Arquivo de entrada não encontrado.");
			return;
		}
		parseInputFile(input);
	}

	private static void parseInputFile(File input) {
		Parser parser = new Parser(input);
		parser.parse();
		//Simulator sim = new Simulator();	
	}
}
