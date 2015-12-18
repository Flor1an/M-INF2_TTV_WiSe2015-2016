package haw;

import de.uniba.wiai.lspi.chord.data.ID;

public class ChordRange {

	private ID start;
	private ID end;
	private State state = State.NOT_HIT;

	/**
	 * Generator for a new instance of an ChordRange
	 * 
	 * @param start
	 *            ID value where the ChordRange starts
	 * @param end
	 *            ID value where the ChordRange ends
	 */
	public ChordRange(ID start, ID end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Checks if an given ID is in this ChordRange
	 *
	 * @param id
	 *            the id that should be checked
	 * @return true, if id is in ChordRange otherwise false
	 */
	public boolean includes(ID id) {
		return id.isInInterval(start, end);

	}

	/**
	 * determine if a ship sunk on this ChordRange
	 * 
	 * @return
	 */
	public boolean isHit() {
		if (state.equals(State.SHIP_DESTROYED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * determine if anyone shot on this ChordRange
	 * 
	 * @return
	 */
	public boolean isShotAt() {
		if (state.equals(State.NOT_HIT)) {
			return false;
		} else {
			return true;
		}
	}

	public ID getEnd() {
		return end;
	}

	public ID getStart() {
		return start;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ChordRange [start=" + start + ", end=" + end + ", state=" + state + "]";
	}
}
