package dacortez.netSimulator;

import dacortez.netSimulator.parser.Parser;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.21
 */
public class Simulator {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java -jar netSimulator.jar <arquivo_de_entrada>");
			return;
		}
		Parser parser = new Parser(args[0]);
		if (parser.parse()) {
			parser.printLinks();
			parser.printHosts();
			parser.printDnsServers();
			parser.printHttpServers();
			parser.printHttpClients();
			parser.printRouters();
			parser.printSimEvents();
		}
	}
}
