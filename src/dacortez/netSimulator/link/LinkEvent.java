package dacortez.netSimulator.link;

import dacortez.netSimulator.network.Datagram;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2012.11.07
 */
public interface LinkEvent {
	void linkEventHandler(Datagram data);
}
