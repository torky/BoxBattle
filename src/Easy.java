import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Easy extends Team {

	public Easy(Color tNumber, int unit0Key, int teamNumber, int popLimit) {
		super(tNumber, unit0Key, teamNumber, popLimit);
	}

	//Code for cursor
	public void actionEasy(ActionEvent e, ArrayList<Team> teams,
			ArrayList<Obstruction> obstructions, Structures structure) {
		
		//Ensure that the units actually don't stay on top of each other
		xStart += r.nextInt(2);
		yStart += r.nextInt(2);
		xStart -= r.nextInt(2);
		yStart -= r.nextInt(2);
		
		Unit u = nearestUnit(totalTroops);
		
		//Spawns a fast unit if there are no units
		if (u == null) {
			spawnUnit(0);
		} else {
			//Spawns units when its next to an allied unit
			if (distanceFromUnit(u) < 10 * 10) {
				if (population < populationLimit) {
					spawnUnit(0);
					spawnUnit(1);
					spawnUnit(2);
				}
			} else {
				//Moves the thing toward the nearest allied unit
				direct(u.x, u.y, 10, "movement");
			}
		}

	}

	//Moves the cursor
	public void direct(float xOther, float yOther, int boingFactor, String type) {
		if (xStart > xOther) {
			xStart -= boingFactor
					* vectorSpeed('x', 1, xStart, yStart, xOther, yOther);
		} else if (xStart <= xOther) {
			xStart += boingFactor
					* vectorSpeed('x', 1, xStart, yStart, xOther, yOther);
		}
		if (yStart > yOther) {
			yStart -= boingFactor
					* vectorSpeed('y', 1, xStart, yStart, xOther, yOther);
		} else if (yStart <= yOther) {
			yStart += boingFactor
					* vectorSpeed('y', 1, xStart, yStart, xOther, yOther);
		}
	}

	//Finds nearest Unit
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

	//Finds distance from unit
	public float distanceFromUnit(Unit u) {
		float distance = 10000;

		if ((u != null) && (u.health > 0)) {
			distance = ((xStart - u.x) * (xStart - u.x) + (yStart - u.y)
					* (yStart - u.y));
			if (((xStart - u.x - u.width) * (xStart - u.x - u.width) + (yStart - u.y)
					* (yStart - u.y)) < distance) {
				distance = ((xStart - u.x - u.width) * (xStart - u.x) + (yStart - u.y)
						* (yStart - u.y));
			}
			if (((xStart - u.x) * (xStart - u.x) + (yStart - u.y - u.height)
					* (yStart - u.y - u.height)) < distance) {
				distance = ((xStart - u.x) * (xStart - u.x) + (yStart - u.y - u.height)
						* (yStart - u.y - u.height));
			}
			if (((xStart - u.x - u.width) * (xStart - u.x - u.width) + (yStart - u.y)
					* (yStart - u.y)) < distance) {
				distance = ((xStart - u.x - u.width) * (xStart - u.x - u.width) + (yStart - u.y)
						* (yStart - u.y));
			}
			return distance;
		} else {
			distance = 10000;
			return distance;
		}
	}

	//Finds the vector speed
	public float vectorSpeed(char vector, float speed, float x, float y,
			float x2, float y2) {

		if (vector == 'x') {
			return (float) (speed * Math.abs(x - x2) / Math.sqrt((x - x2)
					* (x - x2) + (y - y2) * (y - y2)));
		} else if (vector == 'y') {
			return (float) (speed * Math.abs(y - y2) / Math.sqrt((x - x2)
					* (x - x2) + (y - y2) * (y - y2)));
		} else if (vector == 'a') {
			return (float) (speed / 2);
		} else if (vector == 'b') {
			return (float) (speed / 2);
		} else {
			return 0;
		}

	}
}
