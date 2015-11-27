package haw;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;

public class ImplementedNotifyCallback implements NotifyCallback {

	@Override
	public void retrieved(ID target) {
		System.out.println("RETRIEVED AT: \n\t" + target.toString());
	}

	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		System.out.println("BROADCAST: \n\t source: " + source.toString()
									+ "\n\t target: " + target.toString()
									+ "\n\t hit: " + hit.toString());
	}

}
