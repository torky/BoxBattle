
import java.applet.Applet;

public class Main extends Applet{
	
	public static int lengthOfFrame = 1200;
	public static int heightOfFrame = 720;
	
	public static void main(String[] args) {

		Window w = new Window();
		w.setSize(lengthOfFrame, heightOfFrame);
		w.setResizable(false);
		w.setVisible(true);
	}
}