package dacortez.netSimulator.transport;

import java.util.List;

import dacortez.netSimulator.network.HostInterface;
import dacortez.netSimulator.application.Message;
import dacortez.netSimulator.application.Process;
import dacortez.netSimulator.application.Socket;

public class TcpController {
	private Process process;
	private HostInterface hostInterface;
	private List<SegmentTimer> stList;
	private int initialSeqNumber;
	// Maximum segment size (bytes): tamanho máximo de dados que cabe em um segmento.
	private final static int MSS = 1460;
	
	public TcpController(Process process, HostInterface hostInterface) {
		this.process = process;
		this.hostInterface = hostInterface;
	}
	
	public void establishConnection() {
		// TODO Auto-generated method stub		
	}
	
	public void send(Message message) {
		makeFrames(message);
		Socket socket = process.getSocket();
		for (SegmentTimer st: stList) {
			TcpSegment segment = st.getSegment();
			hostInterface.send(segment, socket.getSourceIp(), socket.getDestinationIp());
		}
	}
	
	private void makeFrames(Message message) {
		byte[] messageData = message.toBytes();
		int numberOfSegments = messageData.length / MSS;
		for (int i = 0; i < numberOfSegments; i++) {
			byte[] segmentData = new byte[MSS];
			for (int j = 0; j < MSS; j++)
				segmentData[j] = messageData[j + i * MSS];
			addToList(segmentData, initialSeqNumber + i * MSS);
		}
		makeLastFrame(messageData, numberOfSegments);
	}

	private void makeLastFrame(byte[] messageData, int numberOfSegments) {
		int rest = messageData.length % MSS;
		if (rest > 0) {
			byte[] segmentData = new byte[rest];
			for (int j = 0 ; j < rest; j++)
				segmentData[j] = messageData[j];
			addToList(segmentData, initialSeqNumber + numberOfSegments * MSS);
		}
	}
	
	private void addToList(byte[] data, int seqNumber) {
		Message message = new Message(data);
		Socket socket = process.getSocket();
		TcpSegment segment = new TcpSegment(message, socket.getSourcePort(), socket.getDestinationPort());
		segment.setSeqNumber(seqNumber);
		stList.add(new SegmentTimer(segment));
	}

	public void timeout(TcpSegment segment) {
		
	}
}
