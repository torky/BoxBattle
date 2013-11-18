import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Terrain {

	ArrayList<Obstruction> totalObstructions = new ArrayList<Obstruction>();
	int initMouseX = 0;
	int initMouseY = 0;

	// Which button on the mouse (right or left)
	int mouseClick = 0;

	int length = 0;
	int height = 0;
	int x = 0;
	int y = 0;
	boolean firstClick = true;

	public void reset() {
		totalObstructions.clear();
	}

	public void draw(Graphics g) {
		for (Obstruction b : totalObstructions) {
			if (b != null) {
				if (b.destroyable) {
					if (b.obstructionType == MouseEvent.BUTTON1) {
						g.setColor(Color.WHITE);
					} else if (b.obstructionType == MouseEvent.BUTTON3) {
						g.setColor(Color.LIGHT_GRAY);
					}
				} else {
					if (b.obstructionType == MouseEvent.BUTTON1) {
						g.setColor(Color.DARK_GRAY);
					} else if (b.obstructionType == MouseEvent.BUTTON3) {
						g.setColor(Color.GRAY);
					}
				}
				g.fillRect(b.x, b.y, b.width, b.height);
				if (b.destroyable) {
					g.setColor(Color.CYAN);
					if ((b.width > 0) && (b.height > 0) && (b.initHealth > 0)) {
						g.fillRect(b.x, b.y, (int) ((float) b.health
								/ (float) b.initHealth * (float) b.width),
								(int) 2);

					}
				}
			}
		}
	}

	public void mouseClick(MouseEvent e) {
		for (Obstruction b : totalObstructions) {
			b.delete(true, e.getX(), e.getY());
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePress(MouseEvent e) {
		mouseClick = e.getButton();
		if (firstClick) {
			initMouseX = e.getX();
			initMouseY = e.getY();
			firstClick = false;
		}
	}

	public void mouseRel(MouseEvent e, boolean running) {
		mouseClick = e.getButton();

		firstClick = true;
		length = Math.abs(initMouseX - e.getX());
		height = Math.abs(initMouseY - e.getY());

		int healthiness = length * height * 10;

		if (initMouseX < e.getX()) {
			x = initMouseX;
		} else {
			x = e.getX();
		}
		if (initMouseY < e.getY()) {
			y = initMouseY;
		} else {
			y = e.getY();
		}

		if ((length >= 30) && (height >= 30)) {
			Obstruction b = new Obstruction(x, y, length, height, running,
					healthiness, mouseClick);
			totalObstructions.add(b);
		}
	}

	public void actionHit(ActionEvent e) {

		try {
			for (Obstruction b : totalObstructions) {
				if (b.health <= 0) {
					totalObstructions.remove(totalObstructions.indexOf(b));
				}
			}
		} catch (Exception exception) {

		}
	}

}
