package haw;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Chord;

public class AsyncBroadcast implements Runnable {
	private ID target;
	private boolean hit;
	private Chord chord;

	/**
	 * sends asynchrony an broadcast
	 *
	 * @param chord
	 * @param target
	 * @param hit
	 */
	public AsyncBroadcast(Chord chord, ID target, boolean hit) {
		this.chord = chord;
		this.target = target;
		this.hit = hit;
	}

	@Override
	public void run() {
		if (Starter.PRINT_INTERMEDIATE) {
			System.out.println("\t SENDIG Broadcast: [target=" + target + ", hit=" + hit + "]\n");
		}
		chord.broadcast(target, hit);
	}
}
