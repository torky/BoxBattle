import java.applet.Applet;
import javax.swing.JFrame;

public class Main extends Applet{
	
	public static int lengthOfFrame = 1200;
	public static int heightOfFrame = 720;
	
	public static void main(String[] args) {
		GamePanel gp = new GamePanel();

		Window w = new Window();
		w.setSize(lengthOfFrame, heightOfFrame);
		w.setResizable(false);
		w.add(gp);
		w.setVisible(true);
	}
}
