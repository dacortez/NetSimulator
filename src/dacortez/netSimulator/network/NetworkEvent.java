package dacortez.netSimulator.network;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public interface NetworkEvent {
	void networkEventHandler(Datagram data);
}
