package dacortez.netSimulator.network;

import dacortez.netSimulator.events.EventArgs;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.07
 */
public interface NetworkEventListener {
	void networkEventHandler(EventArgs args);
}
