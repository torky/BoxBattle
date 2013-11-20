import java.util.ArrayList;

public class Building {

	int width;
	int height;
	int x;
	int y;
	int buildingSide = -1;
	int buildingRadius = 70;
	Unit defender;

	public Building(int xpos, int ypos) {
		x = xpos;
		y = ypos;
		width = 50;
		height = 50;
		defender = null;
	}

	public void reset() {
		buildingSide = -1;
		defender = null;
	}

	public void clear() {
		if (defender != null) {
			if (defender.health <= 0) {
				reset();
			}
		}
	}

	public void changeDefender(Unit u) {
		if (u != null) {
			if (distanceFromUnit(u) < buildingRadius * buildingRadius) {
				defender = u;
				buildingSide = u.side;
				u.defending(this);
			}
		}
	}

	public Unit nearestUnit(ArrayList<Unit> u) {
		Unit mob = null;
		for (Unit i : u) {
			if ((i != null)) {
				if (mob == null) {
					mob = i;

				} else if (distanceFromUnit(i) < distanceFromUnit(mob)) {
					if ((mob.health > 0)) {
						mob = i;
					}
				}
			}
		}
		if (mob == null) {
			return mob;

		} else {
			return mob;
		}
	}

	public float distanceFromUnit(Unit u) {
		float distance = 10000;
		if ((u != null) && (u.health > 0)) {
			distance = ((x + width / 2 - u.x) * (x + width / 2 - u.x) + (y
					+ height / 2 - u.y)
					* (y + height / 2 - u.y));
			return distance;
		} else {
			distance = 10000;
			return distance;
		}
	}

	public void actionBuilding(ArrayList<Unit> u) {
		if (u.isEmpty()) {
		} else {
			if (defender == null) {
				changeDefender(nearestUnit(u));
			} else if (defender.health <= 0) {
				defender = null;
				if (nearestUnit(u).notDefending)
					changeDefender(nearestUnit(u));
			} else {
				defender.defending(this);
			}
		}
		System.out.println("Defender:"+defender);
	}

	public boolean pushOut(Unit u) {
		if ((y <= u.y + u.height && y >= u.y - height)
				&& (x <= u.x + u.width && x >= u.x - width)) {
			return true;
		} else {
			return false;
		}
	}
}
