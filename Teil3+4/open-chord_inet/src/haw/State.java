package haw;

public enum State {
	/**
	 * never shot at
	 */
	NOT_HIT,

	/**
	 * shot at but there was no ship
	 */
	SHIP_MISSED,

	/**
	 * shot at and destroyed an ship
	 */
	SHIP_DESTROYED
}
