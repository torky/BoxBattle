import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {

	Menu menu = new Menu();
	
	public Window() {

		super.setTitle("BoxBattle 6.2.1");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(menu);
	}
}
