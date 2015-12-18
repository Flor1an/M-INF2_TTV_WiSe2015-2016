package haw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import de.uniba.wiai.lspi.chord.data.ID;

public class Range {

	private ArrayList<ChordRange> chordRanges;
	private ID rangeStart;
	private ID rangeEnd;
	private boolean ownsApex = false;

	/**
	 * Default generator for a new instance of an Range
	 * 
	 * @param start
	 *            ID value where the range should start. <br />
	 *            Note: <u>This ID will be subtracted <b>by one</b> to avoid
	 *            overlapping.</u>
	 * @param end
	 *            ID value where the range should end
	 */
	public Range(ID start, ID end) {
		rangeStart = ID.valueOf(start.toBigInteger().subtract(BigInteger.ONE));
		rangeEnd = end;
		chordRanges = new ArrayList<ChordRange>();

		initialization();
	}

	/**
	 * Special generator for a new instance of an Range
	 * 
	 * @param propperStart
	 *            start ID where the range should start. <br />
	 *            Note: <u>Its necessary that this ID will not overlap with the
	 *            end of the range of the predecessor.</u>
	 * @param end
	 *            ID value where the range should end
	 * @param x
	 *            something (will be ignored)
	 */
	public Range(ID propperStart, ID end, Object x) {
		rangeStart = propperStart;
		rangeEnd = end;
		chordRanges = new ArrayList<ChordRange>();

		initialization();
	}

	private void initialization() {
		// Values will be converted to BigInteger in order to calculate with the
		// values!
		BigInteger rangeStart = this.rangeStart.toBigInteger();
		BigInteger rangeEnd = this.rangeEnd.toBigInteger();
		BigInteger logicalPositions = new BigInteger(Integer.toString(Starter.AMOUNT_OF_LOGICAL_POSITIONS));

		// determine the apex of the chord ring
		BigInteger maxID = new BigInteger(160, new Random());

		// determine if range include the apex position
		if (ID.valueOf(maxID).isInInterval(this.rangeStart, this.rangeEnd)) {
			ownsApex = true;
		}

		// determine the length the range needs to have
		BigInteger rangeLength = maxID.add(rangeEnd.subtract(rangeStart)).mod(maxID);

		// determine remainder based on the range length
		BigInteger intervallRemainder = rangeLength.mod(logicalPositions);

		// determine the actually underlying chord length
		BigInteger chrordRangeLength = rangeLength.subtract(intervallRemainder).divide(logicalPositions);

		// Iterate over all logical positions
		for (int i = 0; i < Starter.AMOUNT_OF_LOGICAL_POSITIONS; i++) {

			BigInteger startOfChordRange = BigInteger.valueOf(i).multiply(chrordRangeLength).add(rangeStart);
			BigInteger endOfChordRange;
			if (i + 1 == Starter.AMOUNT_OF_LOGICAL_POSITIONS) {
				// last interval goes until end (maybe fill up some more id's)
				endOfChordRange = rangeEnd;
			} else {
				// others go until ChrordRange length * i+1
				endOfChordRange = BigInteger.valueOf(i+1).multiply(chrordRangeLength).add(rangeStart);
			}
			
			//store determine ChrodRange
			chordRanges.add(new ChordRange(ID.valueOf(startOfChordRange), ID.valueOf(endOfChordRange)));
		}
	}

	/**
	 * Gets the ChordRange where an specified target ID is included.
	 *
	 * @param target
	 *            the target ID that are ask for
	 * @return the ChordRange object that includes the target otherwise null
	 */
	public ChordRange getChordRangeOf(ID target) {
		for (ChordRange i : chordRanges) {
			if (i.includes(target)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Checks if an given ID is in this range
	 *
	 * @param id
	 *            the id that should be checked
	 * @return true, if id is in range otherwise false
	 */
	public boolean includes(ID id) {
		return id.isInInterval(rangeStart, rangeEnd);
	}

	public ArrayList<ChordRange> getChordRanges() {
		return chordRanges;
	}

	public ID getRangeStart() {
		return rangeStart;
	}

	public ID getRangeEnd() {
		return rangeEnd;
	}

	public boolean ownsApex() {
		return ownsApex;
	}

	@Override
	public String toString() {
		return "Range [rangeStart=" + rangeStart + ", rangeEnd=" + rangeEnd + ", ownsApex=" + ownsApex + "]";
	}
}
