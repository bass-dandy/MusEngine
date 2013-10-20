import org.jfugue.*;


public class Test2 {
	
	
	public static void main(String args[]) {
		String melody = Test.walkingFifths();
		Player p = new Player();
		Pattern pat = new Pattern(melody);
		IntervalNotation riff = new IntervalNotation(melody);
		pat.add(riff.getPatternForRootNote("C4"));
		pat.insert("T100");
		p.play(pat);
		System.out.println(melody);
	}
	
	

}
