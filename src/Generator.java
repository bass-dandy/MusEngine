import org.jfugue.*;
import java.util.Random;

public class Generator {

	private static final String[] roots = {"A4", "A#4", "B4", "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4"};
	private static final String[] voicings = {"maj", "min", "min", "maj", "maj", "min", "dim"};

	private static final int[] intervals = {0, 2, 4, 5, 7, 9, 11, 12};

	private static final int[] secondToLast = {1, 3, 4};
	private static final int[] majorArp = {0, 4, 7, 12};
	private static final int[] minorArp = {0, 3, 7, 12};
	private static final int[] dimArp = {0, 3, 6, 12};
	private static final int[] susArp = {0, 2, 5, 7};

	// pleasant places for the current chord to move to
	private static final int[] fromRoot = {0, 1, 2, 4};
	private static final int[] from2nd = {0, 1, 2, 3, 5};
	private static final int[] from3rd = {0, 1, 2, 3, 4};
	private static final int[] from4th = {1, 2, 3, 4, 5};
	private static final int[] from5th = {0, 2, 4, 5, 6};
	private static final int[] from6th = {1, 3, 4, 5};
	private static final int[] from7th = {0};
	private static final int[][] sequences = {fromRoot, from2nd, from3rd, from4th, from5th, from6th, from7th};

	// parallel modes, ordered by number of added flats (lydian adds 1 sharp)
	private static final int[] ionian = {0, 2, 4, 5, 7, 9, 11, 12};
	private static final int[] mixolydian = {0, 2, 4, 5, 7, 9, 10, 12};
	private static final int[] dorian = {0, 2, 3, 5, 7, 9, 10, 12};
	private static final int[] aeolian = {0, 2, 3, 5, 7, 8, 10, 12};
	private static final int[] phrygian = {0, 1, 3, 5, 7, 8, 10, 12};
	private static final int[] locrian = {0, 1, 3, 5, 6, 8, 10, 12};
	private static final int[] lydian = {0, 2, 4, 6, 7, 9, 11, 12};;

	private String root = "C";
	private int tempo = 120;
	private String leadInst = "CRYSTAL";
	private String chordsInst = "STRING_ENSEMBLE_2";

	private Player player;
	private Pattern song;

	public Generator()
	{
		player = new Player();
	}

	public void play()
	{
		if(player.isPaused())
			player.resume();
		else if(!player.isStarted())
		{
			String t = "T" + tempo;
			song = writeSong(root, t);
			player.play(song);
		}
	}

	public void pause()
	{
		if(player.isPlaying())
			player.pause();
	}

	public void stop()
	{
		player.stop();
	}

	/**
	 * Writes a song in a specified key at a specified tempo and returns it as a Pattern
	 * 
	 * @param root The key the song will be written in
	 * @param tempo The tempo at which the song will be played
	 * @return Pattern song: The finished product as a JFugue Pattern
	 */
	private Pattern writeSong(String root, String tempo) {
		Pattern chords = new Pattern();
		Pattern lead = new Pattern();
		Pattern song = new Pattern();
		// set length of song (number of measures/2)
		for(int i = 0; i < 1; i++)
		{
			// write lead and chords
			String c = writeChords(root);
			String a = writeLead(c);
			// convert interval notation to something readable by JFugue, add to chord and lead Patterns
			IntervalNotation riff = new IntervalNotation(c);
			chords.add(riff.getPatternForRootNote(root));
			IntervalNotation riff2 = new IntervalNotation(a);
			lead.add(riff2.getPatternForRootNote(root + "6"));
		}
		// add instrument and channel info to each track
		lead.insert("v4 " + "I[" + leadInst + "]");
		chords.insert("v5 " + "I[" + chordsInst + "]");

		// combine tracks in song, set tempo
		song.add(lead);
		song.add(chords);
		song.insert(tempo);
		return song;
	}


	/**
	 * Writes a lead part based on an already finished chord progression
	 * 
	 * @param chords JFugue Pattern string representing a chord progression to be soloed over
	 * @return String melody: JFugue Pattern string representing the completed melody
	 */
	public String writeLead(String chords) {
		String[] chordsArr = chords.split(" ");
		String melody = "";
		int prev = 0;
		int current = 0;
		int interval = 0;
		Random r = new Random();

		// write arps chord by chord
		for(int i = 0; i < chordsArr.length; i++) {
			interval = Integer.parseInt(Character.toString(chordsArr[i].charAt(1)));

			// make major arp
			if(interval == 0 || interval == 5 || interval == 7) {
				for(int j = 0; j < 4; j++) {
					current = r.nextInt(4);
					if(current == prev && j != 0) {
						melody += "q";
					}
					else if(chordsArr[i].substring(3, 7).equals("sus")){
						melody += " <" + (susArp[current] + interval) + ">q";
					}
					else {
						melody += " <" + (majorArp[current] + interval) + ">q";
					}
					prev = current;
				}
			}
			// make dimished arp
			else if(interval == 11) {
				for(int j = 0; j < 4; j++) {
					current = r.nextInt(4);
					if(current == prev && j != 0) {
						melody += "q";
					}
					else {
						melody += " <" + (dimArp[current] + interval) + ">q";
					}
					prev = current;
				}
			}
			// make minor arp
			else {
				for(int j = 0; j < 4; j++) {
					current = r.nextInt(4);
					if(current == prev && (j != 0 && i != 0)) {
						melody += "q";
					}
					else {
						melody += " <" + (minorArp[current] + interval) + ">q";
					}
					prev = current;
				}
			}
		}
		return melody;
	}



	/**
	 * Writes a melody that links two chords together
	 * 
	 * @param source The chord being walked from
	 * @param dest The chord being walked to
	 * @return melody: JFugue Pattern String representing the completed melody
	 */
	public String walkToChord(int source, int dest) {
		String melody = "<";
		int distance = dest - source;

		return melody;
	}


	/**
	 * Creates a randomized chord progression string in specified key
	 * 
	 * @param root The key the song is written in
	 * @return String progression: a JFugue pattern string representing the finished chord progression
	 */
	public String writeChords(String root) {
		String progression = "<0>majw "; // first chord is always the tonic
		Random r = new Random();
		int tmp = 0;
		int prev = 0;

		// middle chords are random
		for(int i = 0; i < 5; i++) {
			// bases current chord on the previous chord
			int[] current = sequences[prev];
			tmp = r.nextInt(current.length);
			tmp = current[tmp];
			progression += buildChord(tmp, 'w'); // add next chord
			prev = tmp; // save chord added for building the next chord
		}
		// 2nd to last chord will be a pleasant cadence
		tmp = r.nextInt(3);
		progression += buildChord(secondToLast[tmp], 'w');
		// last chord will be the tonic
		progression += "<0>majw";
		return progression;
	}	


	/**
	 * Builds a chord at a specified interval that will sustain for a specified duration
	 * 
	 * @param interval The interval at which the chord will be constructed
	 * @param dur How many beats the chord will be sustained
	 * @return String chord: JFugue Pattern string representing the completed chord
	 */
	private String buildChord(int interval, char dur) {
		Random r = new Random();
		// if it's a 5th, roll die to replace with sus4
		if(interval == 4) {
			int sub = r.nextInt(2);
			if(sub == 1) {
				return "<" + intervals[interval] + ">sus4" + dur + " "; 
			}
		}
		String chord = "<" + intervals[interval] + ">" + voicings[interval] + dur + " ";
		return chord;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public void setLeadInst(String leadInst) {
		this.leadInst = leadInst;
	}

	public void setChordsInst(String chordsInst) {
		this.chordsInst = chordsInst;
	}
}
