
import java.io.IOException;


public class Main
{
	public static void main(String[] args) throws IOException
	{
		Generator g = new Generator();
		ControlPanel cp = new ControlPanel(g);
		cp.show();
	}
}
