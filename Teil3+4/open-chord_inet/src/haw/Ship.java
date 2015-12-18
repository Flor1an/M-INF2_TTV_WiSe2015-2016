package haw;

public class Ship {

	int logicalPosition;
	ChordRange chordRange;

	/**
	 * Default generator for an new instance of an ship
	 * 
	 * @param logicalPosition
	 *            represents an number where the ship is located
	 */
	public Ship(int logicalPosition) {
		this.logicalPosition = logicalPosition;
	}

	/**
	 * Specialized generator for an new instance of an ship
	 * 
	 * @param logicalPosition
	 *            represents an number where the ship is located
	 * 
	 * @param chordRange
	 *            represents an range of the underlying chord structure
	 */
	public Ship(int logicalPosition, ChordRange chordRange) {
		this.logicalPosition = logicalPosition;
		this.chordRange = chordRange;
	}

	public int getLogicalPosition() {
		return logicalPosition;
	}

	public ChordRange getChordRange() {
		return chordRange;
	}

	/**
	 * sets the underlying range
	 * 
	 * @param chordRange
	 */
	public void setChordRange(ChordRange chordRange) {
		this.chordRange = chordRange;
	}

	@Override
	public String toString() {
		return "Ship [logicalPosition=" + logicalPosition + ", \n\t\tchordRange=" + chordRange + "]";
	}

}
