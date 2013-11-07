package dacortez.netSimulator.network;

import dacortez.netSimulator.Interface;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public interface NetworkEvent {
	void networkEventHandler(Interface sender, Datagram data);
}
