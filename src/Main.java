
import javax.swing.SwingUtilities;


public class Main
{
	public static void main(String[] args)
	{
		final Generator g = new Generator();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ControlPanel cp = new ControlPanel(g);
				cp.show();	
			}
		});
	}
}
