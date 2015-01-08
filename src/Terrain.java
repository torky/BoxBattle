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

	//reset
	public void reset() {
		totalObstructions.clear();
	}

	//How terrain is drawn
	public void draw(Graphics g) {
		for (Obstruction b : totalObstructions) {
			if (b != null) {
				if (b.destroyable) {
					//Color if vulnerable wall
					if (b.obstructionType == MouseEvent.BUTTON1) {
						g.setColor(Color.WHITE);
					//Color if vulnerable bog
					} else if (b.obstructionType == MouseEvent.BUTTON3) {
						g.setColor(Color.LIGHT_GRAY);
					}
				} else {
					//Color if invulnerable wall
					if (b.obstructionType == MouseEvent.BUTTON1) {
						g.setColor(Color.DARK_GRAY);
					//Color if imvulnerable bog
					} else if (b.obstructionType == MouseEvent.BUTTON3) {
						g.setColor(Color.GRAY);
					}
				}
				//Health bar
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

	//Deleting terrain with a mouse click
	public void mouseClick(MouseEvent e) {
		for (Obstruction b : totalObstructions) {
			b.delete(true, e.getX(), e.getY());
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	//First Click (chooses type of terrain)
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

		//Setting up the health of the terrain
		int healthiness = length * height * 10;

		//Setting up the terrain
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

		//Creating the terrain
		if ((length >= 30) && (height >= 30)) {
			Obstruction b = new Obstruction(x, y, length, height, running,
					healthiness, mouseClick);
			totalObstructions.add(b);
		}
	}

	public void actionHit(ActionEvent e) {
		//Death of obstructions
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
