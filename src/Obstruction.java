public class Obstruction {

	int width;
	int height;
	int x;
	int y;
	int health;
	int initHealth;
	int obstructionType;
	boolean destroyable;

	//Initializing the obstruction
	public Obstruction(int xpos, int ypos, int Width, int Height, boolean canDie,
			int totalhealth, int type) {

		x = xpos;
		y = ypos;
		width = Width;
		height = Height;
		destroyable = canDie;
		obstructionType = type;

		if (destroyable) {
			health = totalhealth;
			initHealth = totalhealth;
		} else {
			health = 100;
			initHealth = 100;
		}
	}

	//Obstructing the units
	public boolean pushOut(Unit u) {
		if ((y <= u.y + u.height && y >= u.y - height)
				&& (x <= u.x + u.width && x >= u.x - width)) {
			if (destroyable) {
				health -= u.damage;
			}
			return true;
		} else {
			return false;
		}
	}

	//Deleting the obstruction
	public void delete(boolean on, int xpos, int ypos) {
		if (on) {
			if ((y <= ypos && y >= ypos - height)
					&& (x <= xpos && x >= xpos - width)) {

				health = 0;

			}
		}
	}
}
