package haw;

import de.uniba.wiai.lspi.chord.data.ID;

public class Player {

	private ID playerID;
	private Range range;
	private int lostShips;

	public Player(ID id) {
		playerID = id;
	}

	public ID getPlayerID() {
		return playerID;
	}

	public void setPlayerRange(Range range) {
		this.range = range;
	}

	public Range getPlayerRange() {
		return range;
	}

	public int getLostShips() {
		return lostShips;
	}

	public void setLostShips(int lostShips) {
		this.lostShips = lostShips;
	}

	public void increaseLostShipCount() {
		lostShips++;
	}

	@Override
	public String toString() {
		return "Player [playerID=" + playerID + ", range=" + range + ", lostShips=" + lostShips + "]";
	}
}
