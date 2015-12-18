package haw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Sea {

	private Chord chord;
	private Strategy strategy;
	private ArrayList<Ship> listOfOwnShips;
	private ArrayList<ID> listOfIdsOfPlayers;
	private Range rangeOfMyNode;
	private HashMap<ID, Player> mapOfPlayers;
	private ID myID;
	private HashMap<ID, State> mapOfTargetsSomeoneShootTo;

	private static Sea instance;

	public static Sea getInstance() {
		if (Sea.instance == null) {
			Sea.instance = new Sea();
		}
		return Sea.instance;
	}

	private Sea() {
		this.strategy = new AdvancedRandomStrategy();
		this.listOfOwnShips = new ArrayList<Ship>();
		this.listOfIdsOfPlayers = new ArrayList<ID>();
		this.mapOfPlayers = new HashMap<ID, Player>();
		this.mapOfTargetsSomeoneShootTo = new HashMap<ID, State>();
	}

	/**
	 * very first entry point to start of the actually game logic. (Includes
	 * initialization and start of the game)
	 */
	public void startGame() {
		if (chord != null) {
			// initialize the world. Calculate Ranges, set ships, ...
			initializationOfTheSea();

			System.err.println("\n\nGAME STARTED");

			// determine if this is the 1st Player. If so: open fire on others
			if (isBeginner()) {
				System.err.println("\nThis player is the 'hightest player'.");

				// waiting for user confirmation to open the game.
				System.err.println("Press enter to start shooting.\n\n");
				new Scanner(System.in).nextLine();

				// do the very first shoot.
				shoot();
			} else {
				System.err.println("\nWaiting for 'hightest player' to shoot somewhere (waiting to recive a broadcast)...\n\n");
			}

		} else {
			System.err.println("Chord not set!");
		}
	}

	/**
	 * base on the strategy an target gets retrieved. This target gets "shoot"
	 * (will call the chord retrieve method)
	 */
	private void shoot() {
		try {
			System.out.println("\nWE ARE SHOOTING:");

			ID target = strategy.determineTarget();

			System.out.println("\tShooting at: " + target + "\n\n");

			// call the retrieve method in order to perform the shoot (via an
			// broadcast)
			chord.retrieve(target);

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * perform the mandatory initialization steps: <br />
	 * - joining of player <br />
	 * - calculating ranges <br />
	 * - filling of my range with ships (based on my strategy)
	 */
	private void initializationOfTheSea() {
		this.myID = chord.getID();
		joiningPlayers();
		calculateRanges();
		fillMyRangeWithShips();
	}

	/**
	 * Adding all players that I'm aware of already during initialization
	 */
	private void joiningPlayers() {
		System.out.println("JOINING INITIAL PLAYERS:");
		// add myself to the list of known players
		joinPlayerById(myID);

		// add all records from finger table to the list of known players
		ChordImpl c = (ChordImpl) chord;
		List<Node> myFingerTable = c.getFingerTable();
		for (Node n : myFingerTable) {
			joinPlayerById(n.getNodeID());
		}

		// add my predecessor to the list of known players
		joinPlayerById(c.getPredecessorID());
	}

	/**
	 * Adds the playerId to the internal data structures
	 * 
	 * @param playerId
	 */
	public void joinPlayerById(ID playerId) {
		// only "new" players will be added / recognized as opponents
		if (playerId != null && !listOfIdsOfPlayers.contains(playerId)) {
			listOfIdsOfPlayers.add(playerId);
			mapOfPlayers.put(playerId, new Player(playerId));

			printPlayerJoined(playerId);
		}

	}

	/**
	 * making assumptions of the responsible ranges of all players thru
	 * calculation with the amount of known players (which is not necessary the
	 * proper list, but the best we can know for the current game progress)
	 */
	public void calculateRanges() {
		System.out.println("\nCALCULATING RANGES FOR ALL PLAYERS:");
		Collections.sort(listOfIdsOfPlayers);

		for (int i = 0; i < listOfIdsOfPlayers.size(); i++) {
			ID endIdOfRange = listOfIdsOfPlayers.get(i);

			// determine the predecessor
			ID startIdOfRange;
			if (i == 0) {
				startIdOfRange = listOfIdsOfPlayers.get(listOfIdsOfPlayers.size() - 1);
			} else {
				startIdOfRange = listOfIdsOfPlayers.get(i - 1);
			}

			// Instantiate the range
			Range ra = new Range(startIdOfRange, endIdOfRange);

			// determine the range of this node
			if (endIdOfRange.equals(chord.getID())) {
				rangeOfMyNode = ra;
			}

			// bind the range to the player instance
			mapOfPlayers.get(endIdOfRange).setPlayerRange(ra);

			printCalculatedRange(endIdOfRange);
		}

	}

	/**
	 * using the strategy to determine the logical positions of my own ship
	 */
	private void fillMyRangeWithShips() {
		listOfOwnShips = strategy.placeMyShipsOnLogicalPositions();
		recalculateShipPositions();
	}

	/**
	 * Maps the logical positions (that were set by the strategy) to the
	 * position of the underlying chord nodes (that was calculated by the
	 * range). These information will be stored directly in the ship
	 */
	private void recalculateShipPositions() {
		System.out.println("\nMAPPING LOGICAL POSITION OF SHIPS TO RANGE OF UNDERLAYING NODES");

		for (Ship s : listOfOwnShips) {

			ArrayList<ChordRange> myChordRanges = rangeOfMyNode.getChordRanges();

			// gets the ChordRange where the ship should get placed
			ChordRange cr = myChordRanges.get(s.getLogicalPosition());

			// store this information on the ship object
			s.setChordRange(cr);

			System.out.println("\t" + s);
		}
	}

	/**
	 * Determine whether this node is the beginner of the game
	 * 
	 * @return true if its the beginner otherwise false
	 */
	private boolean isBeginner() {
		if (chord.getPredecessorID() == null) {
			// workaround for just one player (but game play itself makes no
			// sense
			// with just one player because the strategy may not be aware about
			// this fact)
			return true;
		} else {
			// maxID represents the apex of the chord
			BigInteger maxID = new BigInteger("2").pow(160).subtract(BigInteger.ONE);
			// determine whether the my range includes the apex point
			return ID.valueOf(maxID).isInInterval(rangeOfMyNode.getRangeStart(), chord.getID());
		}
	}

	/**
	 * if this node retries an shoot on his range this method will be called
	 * from the ImplementedNotifyCallback.java.
	 * 
	 * it determine if the shoot hit one of our ships or not and informs
	 * everyone via broadcast about the result
	 * 
	 * @param target
	 *            will be passes by the retrieve function
	 */
	public void shotDetectedInTheAreaOfThisNode(ID target) {
		try {
			boolean oneShipDestroyed = false;

			// check for every ship if the target was on the underlying range
			// which means the potential ship on it was destroyed
			for (Ship s : listOfOwnShips) {
				ChordRange chordRange = s.getChordRange();

				if (chordRange.includes(target)) {
					if (!chordRange.isHit()) {
						chordRange.setState(State.SHIP_DESTROYED);
						oneShipDestroyed = true;
					}
				}
			}

			// statistic of our ships
			int shipsRemaining = 0;
			for (Ship s : listOfOwnShips) {
				if (Starter.PRINT_MAJOR) {
					System.out.println("\t" + s);
				}
				if (!s.getChordRange().isHit()) {
					shipsRemaining++;
				}
			}

			if (oneShipDestroyed) {
				printShipGotDestroyed(target, shipsRemaining);

				if (Starter.PRINT_INTERMEDIATE) {
					System.out.println("\n\nINFORMING OTHER PLAYERS ABOUT THE DESTRUCTION:");
				}
				Thread.sleep(Starter.SLOWDOWN_IN_MS);

				// broadcast hit information asynchrony
				Thread t = new Thread(new AsyncBroadcast(chord, target, true));
				t.start();

				System.out.println("\n\n");

			} else {
				printHitWater(target, shipsRemaining);
				Thread.sleep(Starter.SLOWDOWN_IN_MS);

				// broadcast hit information asynchrony
				Thread t = new Thread(new AsyncBroadcast(chord, target, false));
				t.start();
			}

			Thread.sleep(Starter.SLOWDOWN_IN_MS);

			// after an shoot in our area we are allowed to shoot again
			shoot();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setChord(Chord chord) {
		this.chord = chord;
	}

	public ID getMyID() {
		return myID;
	}

	public Range getRangeOfMyNode() {
		return rangeOfMyNode;
	}

	public HashMap<ID, Player> getMapOfPlayers() {
		return mapOfPlayers;
	}

	public HashMap<ID, State> getMapOfTargetsSomeoneShootTo() {
		return mapOfTargetsSomeoneShootTo;
	}

	public void setMapOfTargetsSomeoneShootTo(HashMap<ID, State> mapOfTargetsSomeoneShootTo) {
		this.mapOfTargetsSomeoneShootTo = mapOfTargetsSomeoneShootTo;
	}

	private void printPlayerJoined(ID playerId) {
		String myself = "";
		if (playerId.equals(myID)) {
			myself = " <-- MYSELF";
		}
		System.out.println("\tJoining new player " + listOfIdsOfPlayers.size() + " ID: " + playerId + myself);
	}

	private void printCalculatedRange(ID playerId) {
		String myself = "";
		if (playerId.equals(chord.getID())) {
			myself = "<-- MYSELF";
		}
		System.out.println("\t" + "{ ID: " + mapOfPlayers.get(playerId).getPlayerID() + " " + myself + "\n\t"
				+ mapOfPlayers.get(playerId).getPlayerRange() + " }\n");
	}

	private void printShipGotDestroyed(ID target, int shipsRemaining) {
		if (Starter.PRINT_INTERMEDIATE) {
			for (Ship s : listOfOwnShips) {
				System.out.println("\t" + s);
			}
		}
		System.out.println("\tOne ship on " + target + " got destroyed by the shot! (still remaining: " + shipsRemaining + "/"
				+ Starter.AMOUNT_OF_SHIPS + " ships)");
	}

	private void printHitWater(ID target, int shipsRemaining) {
		System.out.println("\tThe Shot at " + target + " hit just water! (remaining: " + shipsRemaining + "/" + Starter.AMOUNT_OF_SHIPS + " ships)");
		if (Starter.PRINT_INTERMEDIATE) {
			System.out.println("\n\nINFORMING OTHER PLAYERS ABOUT THE FAIL:");
		}
	}
}
