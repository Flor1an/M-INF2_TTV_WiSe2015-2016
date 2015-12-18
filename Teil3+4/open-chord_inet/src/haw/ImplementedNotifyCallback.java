package haw;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;

public class ImplementedNotifyCallback implements NotifyCallback {

	@Override
	public void retrieved(ID target) {
		System.out.println("\n\n\nRECIVED A SHOT IN OUR AREA AT: \n\t" + target.toString());
		Sea.getInstance().shotDetectedInTheAreaOfThisNode(target);
	}

	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		if (!source.equals(Sea.getInstance().getMyID())) {
			// If a new player "joins the game": add new user to player list and
			// recalculate the ranges of everyone
			if (!Sea.getInstance().getMapOfPlayers().containsKey(source)) {
//				Sea.getInstance().joinPlayerById(source);
//				Sea.getInstance().calculateRanges();
			}
		}
	}

}
