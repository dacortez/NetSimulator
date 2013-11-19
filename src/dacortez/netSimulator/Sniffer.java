package dacortez.netSimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import dacortez.netSimulator.events.EventArgs;
import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.Interface;
import dacortez.netSimulator.network.NetworkEventListener;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public class Sniffer implements NetworkEventListener {
	// Nome textual do sniffer.
	private String name;
	// Nome do arquivo de saída.
	private String file;
	// Interfaces entre os quais o sniffer fará o monitoramento da rede.
	private Interface point1; 
	private Interface point2;
	// PrintStream utilizado para imprimir dados em arquivo.
	private PrintStream ps;

	public String getName() {
		return name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
		setupPrintStream();
	}

	private void setupPrintStream() {
		try {
			ps = new PrintStream(new File(file));
		} catch (FileNotFoundException e) {
			System.err.println("Erro ao criar arquivo de captura de dados do Sniffer " + name + ".");
			System.err.println("(Os dados capturados serão apresentados apenas na saída padrão).");
			ps = null;
		}
	}

	public void setPoint1(Interface point1) {
		this.point1 = point1;
	}

	public void setPoint2(Interface point2) {
		this.point2 = point2;
	}
	
	public Sniffer(String name) {
		this.name = name;
	}
	
	@Override
	public void networkEventHandler(EventArgs args) {
		String output = getOutput(args);
		System.out.println(output);
		if (ps != null) ps.println(output); 
	}
	
	private String getOutput(EventArgs args) {
		Datagram data = args.getDatagram();
		StringBuilder sb = new StringBuilder();
		sb.append("===============================================================================================\n");
		sb.append("Sniffer: " + name + "\n");
		sb.append("Instante da captura: " + args.getTime() + "\n");
		sb.append(data);
		sb.append("\n===============================================================================================\n");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return name + ": [" + point1 + ", " + point2 + "] >> '" + file + "'";
	}
}