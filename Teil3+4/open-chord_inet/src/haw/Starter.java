package haw;

import java.util.Scanner;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

/**
 * <b>Starter of Sea Battle</b> 
 *
 */
public class Starter {

	public final static int AMOUNT_OF_LOGICAL_POSITIONS = 100; //I
	public final static int AMOUNT_OF_SHIPS = 10; //S
	
	public final static int SLOWDOWN_IN_MS = 0;
	public final static boolean PRINT_FINE_GRAIN_SIZE = false;
	public final static boolean PRINT_INTERMEDIATE = false;
	public final static boolean PRINT_MAJOR = false;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.flush();
		PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		Chord chord = new ChordImpl();
		chord.setCallback(new ImplementedNotifyCallback());

		try {
			URL serverURL = new URL(protocol + "://" + args[1].toString().trim() + "/");
			System.out.println("SERVER: " + protocol + "://" + args[1].toString().trim() + "/");
			if (args[0].equalsIgnoreCase("create")) {
				// create
				chord.create(serverURL);

			} else if (args[0].equalsIgnoreCase("join")) {
				// join

				URL clientURL = new URL(protocol + "://" + args[2].toString().trim() + "/");
				System.out.println("CLIENT: " + protocol + "://" + args[2].toString().trim() + "/");
				chord.join(clientURL, serverURL);

			} else {
				System.err.println("Error! wrong Parameter");
				return;
			}

		} catch (Exception e) {
			throw new RuntimeException(" Could not join DHT ! ", e);
		}
		
		// --------------
		
		Sea.getInstance().setChord(chord);
		System.out.println("\n\n                              __    __    __\n                             |==|  |==|  |==|\n"
				+ "                           __|__|__|__|__|__|_\n                        __|___________________|___\n"
				+ "                     __|__[]__[]__[]__[]__[]__[]__|___\n                    |............................o.../\n"
				+ "                    \\.............................../\n               ~')_,~')_,~')_,~')_,~')_,~')_,~')_,~')/,~')_\n\n"
				+ "                                 Sea Battle");
		System.out.println("\n\n\n\nPress enter to start initialization\n");
		new Scanner(System.in).nextLine();
		System.out.println("Game start");
		Sea.getInstance().startGame();
	}

}
