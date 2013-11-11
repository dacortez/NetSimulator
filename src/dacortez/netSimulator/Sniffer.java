package dacortez.netSimulator;

import dacortez.netSimulator.network.Datagram;
import dacortez.netSimulator.network.Interface;
import dacortez.netSimulator.network.NetworkEvent;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.10.20
 */
public class Sniffer implements NetworkEvent {
	// Nome textual do sniffer.
	private String name;
	// Nome do arquivo de saída.
	private String file;
	// Interfaces entre os quais o sniffer fará o monitoramento da rede.
	private Interface point1; 
	private Interface point2;

	public String getName() {
		return name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
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
	public void networkEventHandler(Datagram data) {
		System.out.println("Sniffer " + name + " capturou datagrama [" + data.getId() + "]:");
		System.out.println(data);
		System.out.println("[FAZER SAÍDA EM ARQUIVO]");
		System.out.println(); 
	}
	
	@Override
	public String toString() {
		return name + ": [" + point1 + ", " + point2 + "] >> '" + file + "'";
	}
}