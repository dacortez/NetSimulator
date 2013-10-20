package dacortez.netSimulator.network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.10.20
 */
public class Router {
	// Nome do roteador.
	private String name;
	// Número de interfaces do roteadror.
	private int numberOfInterfaces; 
	// Lista de interfaces do roteador.
	private List<RouterInterface> interfaces;
	// Tempo em para processar um pacote em us.
	private double processingTime; 
	
	public String getName() {
		return name;
	}

	public int getNumberOfInterfaces() {
		return numberOfInterfaces;
	}

	public List<RouterInterface> getInterfaces() {
		return interfaces;
	}
	
	public double getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(double processingTime) {
		this.processingTime = processingTime;
	}
	
	public Router(String name, int numberOfInterfaces) {
		this.name = name;
		this.numberOfInterfaces = numberOfInterfaces;
		interfaces = new ArrayList<RouterInterface>(numberOfInterfaces);
	}
}
