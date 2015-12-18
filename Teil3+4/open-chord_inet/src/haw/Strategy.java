package haw;

import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;

public interface Strategy {

	/**
	 * determine logical ship position not taking in consideration how big the
	 * ranges are set (due to just logical positions). These positions must be
	 * mapped to the underlying chord ranges
	 * 
	 * @return ArrayList of Ships placed on logical positions
	 */
	public ArrayList<Ship> placeMyShipsOnLogicalPositions();

	/**
	 * Will generate an ID to represents an possible target
	 * 
	 * @return target ID
	 */
	public ID determineTarget();
}
