import java.awt.event.MouseEvent;
import java.util.*;

public class Unit {

	int width;
	int height;
	float x;
	float y;
	float speed;
	float initSpeed;
	float health;
	float initHealth;
	int damage;
	float range;
	int reload;
	int reloadTime;
	int boing; // Bounce off power!
	int type;
	int soldierPop;
	int kills;
	boolean returnedKills = true;
	boolean notDefending = true;
	int side;
	int killer = -1;
	int augmentPower = 0;
	int armor = 0;

	Team team;

	Random r = new Random();

	boolean attack = false;

	// Initializing the Unit and choosing its qualities
	public Unit(int kind, Team team, int whichSide) {
		type = kind;
		side = whichSide;
		notDefending = true;
		// Fast Unit
		if (type == 0) {
			width = 10;
			height = 10;
			speed = 0.5F;
			initSpeed = 0.5F;
			health = 250;
			initHealth = 250;
			armor = 0;
			damage = 20;
			range = 10;
			reload = 0;
			reloadTime = 100;
			boing = 20;
			soldierPop = 1;
		}
		// Strong Unit
		else if (type == 1) {
			width = 10;
			height = 10;
			speed = 0.35F;
			initSpeed = 0.35F;
			health = 400;
			initHealth = 400;
			armor = 5;
			damage = 25;
			range = 10;
			reload = 0;
			reloadTime = 100;
			boing = 10;
			soldierPop = 2;
		}
		// Ranged Unit
		else if (type == 2) {
			width = 10;
			height = 10;
			speed = 0.25F;
			initSpeed = 0.25F;
			health = 40;
			initHealth = 40;
			armor = 0;
			damage = 20;
			range = 150;
			reload = 0;
			reloadTime = 100;
			boing = 30;
			soldierPop = 1;
		}
		// Wall Unit
		else if (type == 3) {
			width = 30;
			height = 30;
			speed = 0;
			initSpeed = 0;
			health = 600;
			initHealth = 600;
			armor = 10;
			damage = 0;
			range = 0;
			reload = 0;
			reloadTime = 100;
			boing = 0;
			soldierPop = 0;
		}

	}

	// This makes the unit run
	public void start(ArrayList<Unit> u, ArrayList<Unit> ally, int xlength,
			int yheight, int xstart, int ystart,
			ArrayList<Obstruction> obstructions) {
		for (Obstruction o : obstructions) {
			obstruct(o);
		}
		if ((nearestEnemy(u) != null) && (this != null)) {
			for (Unit i : u) {
				collision(i);
			}
			for (Unit i : ally) {
				if (i != this) {
					collision(i);
				}
			}
			if (notDefending) {
				move(nearestEnemy(u));
			}
			attack(nearestEnemy(u));
			if (health > 0) {
				beingDamaged(u);
			}
		}
		reload();
		restrict(xlength, yheight, xstart, ystart);
		// Old code
		resetKills();

	}

	// Just to free it from its base (maybe utilized in the future)
	public void notDefending() {
		notDefending = true;
	}

	// This keeps the Unit in the base
	public void defending(Building b) {
		notDefending = false;
		x = b.x + b.width / 2 - width / 2;
		y = b.y + b.height / 2 - height / 2;
	}

	// The Unit Colliding and then moving away
	public void direct(float xOther, float yOther, int width, int height,
			int boingFactor, String type) {
		if (type == "movement") {
			if (x > xOther) {
				x += boingFactor
						* vectorSpeed('x', speed, x, y, xOther, yOther);
			} else if (x <= xOther) {
				x -= boingFactor
						* vectorSpeed('x', speed, x, y, xOther, yOther);
			}
			if (y > yOther) {
				y += boingFactor
						* vectorSpeed('y', speed, x, y, xOther, yOther);
			} else if (y <= yOther) {
				y -= boingFactor
						* vectorSpeed('y', speed, x, y, xOther, yOther);
			}
		} else if (type == "obstruct") {
			if (x + this.width / 2 > xOther + width / 2) {
				x += boingFactor
						* vectorSpeed('a', speed, x, y, xOther, yOther);
			} else if (x + this.width / 2 <= xOther + width / 2) {
				x -= boingFactor
						* vectorSpeed('a', speed, x, y, xOther, yOther);
			}
			if (y + this.height / 2 > yOther + height / 2) {
				y += boingFactor
						* vectorSpeed('b', speed, x, y, xOther, yOther);
			} else if (y + this.height / 2 <= yOther + height / 2) {
				y -= boingFactor
						* vectorSpeed('b', speed, x, y, xOther, yOther);
			}
		}
	}

	// Obstructing the Unit in the terrain
	public void obstruct(Obstruction o) {
		if (o.pushOut(this)) {
			if (o.obstructionType == MouseEvent.BUTTON1) {
				direct(o.x, o.y, o.width, o.height, boing / 5, "obstruct");
			}
			if (o.obstructionType == MouseEvent.BUTTON3) {
				if ((y + height <= o.y + o.height && y >= o.y)
						&& (x + width <= o.x + o.width && x >= o.x)) {
					speed = initSpeed / 4;
				} else {
					speed = initSpeed;
				}
			}
		} else {

		}
	}

	// This prevents the Units from clumping
	public void collision(Unit mob) {
		if (mob != null) {
			if ((mob.health > 0)) {
				if ((y <= mob.y + mob.height && y >= mob.y - height)
						&& (x <= mob.x + mob.width && x >= mob.x - width)) {
					direct(mob.x, mob.y, mob.width, mob.height, 1, "movement");
					/*
					 * if (x > mob.x) { x += vectorSpeed('x', speed, x, y,
					 * mob.x, mob.y); } else if (x <= mob.x) { x -=
					 * vectorSpeed('x', speed, x, y, mob.x, mob.y); } if (y >
					 * mob.y) { y += vectorSpeed('y', speed, x, y, mob.x,
					 * mob.y); } else if (y <= mob.y) { y -= vectorSpeed('y',
					 * speed, x, y, mob.x, mob.y); }
					 */
				}
			}
		}
	}

	// Gives the Unit Armor
	public void buildingAura(Building b) {
		if ((b != null) && (b.buildingSide == side)) {
			if ((y <= b.y + b.height && y >= b.y - height)
					&& (x <= b.x + b.width && x >= b.x - width)) {
				augmentPower = 5;
			} else {
				augmentPower = 0;
			}
		}
	}

	// This actually finds the nearest unit of an Array
	public Unit nearestEnemy(ArrayList<Unit> u) {
		Unit mob = null;
		for (Unit i : u) {
			if ((i != null)) {
				if (mob == null) {
					mob = i;

				} else if ((distanceFromUnit(i) < distanceFromUnit(mob))
						&& (mob != this)) {
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

	// This finds the distance squared between the unit and another
	public float distanceFromUnit(Unit u) {
		float distance = 10000;
		float distance1;
		float distance2;
		float distance3;
		if ((u != null) && (u.health > 0)) {
			distance = ((x - u.x) * (x - u.x) + (y - u.y) * (y - u.y));
			if (((x - u.x - u.width) * (x - u.x - u.width) + (y - u.y)
					* (y - u.y)) < distance) {
				distance = ((x - u.x - u.width) * (x - u.x) + (y - u.y)
						* (y - u.y));
			}
			if (((x - u.x) * (x - u.x) + (y - u.y - u.height)
					* (y - u.y - u.height)) < distance) {
				distance = ((x - u.x) * (x - u.x) + (y - u.y - u.height)
						* (y - u.y - u.height));
			}
			if (((x - u.x - u.width) * (x - u.x - u.width) + (y - u.y)
					* (y - u.y)) < distance) {
				distance = ((x - u.x - u.width) * (x - u.x - u.width) + (y - u.y)
						* (y - u.y));
			}
			return distance;
		} else {
			distance = 10000;
			return distance;
		}
	}

	// This finds the speed of the vector(x or y) of the unit
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

	// Moves the Unit in a direction
	// Right
	public void xPlus() {
		x += speed * boing;
	}

	// Left
	public void xMinus() {
		x -= speed * boing;
	}

	// Down
	public void yPlus() {
		y += speed * boing;
	}

	// Up
	public void yMinus() {
		y -= speed * boing;
	}

	// This moves the unit
	public void move(Unit mob) {
		if ((mob != null) && (health > 0) && (mob != this) && (mob.health > 0)) {
			if (type != 2) {
				if (distanceFromUnit(mob) > (range - 3) * (range - 3)) {
					if (x > mob.x) {
						x -= vectorSpeed('x', speed, x, y, mob.x, mob.y);
					} else if (x <= mob.x) {
						x += vectorSpeed('x', speed, x, y, mob.x, mob.y);
					}

					if (y > mob.y) {
						y -= vectorSpeed('y', speed, x, y, mob.x, mob.y);
					} else if (y <= mob.y) {
						y += vectorSpeed('y', speed, x, y, mob.x, mob.y);
					}
				}
			} else {
				if (distanceFromUnit(mob) > (range - 20) * (range - 20)) {
					if (x > mob.x) {
						x -= vectorSpeed('x', speed, x, y, mob.x, mob.y);
					} else if (x <= mob.x) {
						x += vectorSpeed('x', speed, x, y, mob.x, mob.y);
					}

					if (y > mob.y) {
						y -= vectorSpeed('y', speed, x, y, mob.x, mob.y);
					} else if (y <= mob.y) {
						y += vectorSpeed('y', speed, x, y, mob.x, mob.y);
					}
				}
			}
		}
	}

	// This signals if the unit is attacking
	public boolean attack(Unit mob) {
		if ((mob != null) && (mob.health > 0)) {

			if ((distanceFromUnit(mob) <= (range + 2) * (range + 2))
					&& (reload <= 0)) {
				reload = reloadTime;
				if (x > mob.x) {
					x += boing * vectorSpeed('x', speed, x, y, mob.x, mob.y);
				} else if (x <= mob.x) {
					x -= boing * vectorSpeed('x', speed, x, y, mob.x, mob.y);
				}

				if (y > mob.y) {
					y += boing * vectorSpeed('y', speed, x, y, mob.x, mob.y);
				} else if (y <= mob.y) {
					y -= boing * vectorSpeed('y', speed, x, y, mob.x, mob.y);
				}
				if ((mob.health <= damage) && (mob.health >= 10)) {
					kills++;
					System.out.println("kills:" + kills);
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	//Adding Kills for the Unit (A bit useless)
	public int addKills() {

		if (returnedKills) {
			kills = 0;
		}
		if (kills > 0) {
			returnedKills = true;
			return kills;
		} else {
			returnedKills = false;
			return 0;
		}

	}

	public void resetKills() {
		if (returnedKills) {
			kills = 0;
			returnedKills = false;
		}
	}

	// This method deals the specified damage to the Unit itself
	public void beingDamaged(ArrayList<Unit> mobArray) {
		for (Unit mob : mobArray) {
			if (mob != null)
				if ((mob.attack(this))) {
					health -= (mob.damage - armor - augmentPower);
					if (health <= 0) {
						killer = mob.side;
					}
				}

		}
	}

	// This reloads the unit's next attack
	public void reload() {
		reload--;
	}

	// This prevents the unit from running off the map
	public void restrict(int length, int height, int xstart, int ystart) {
		if (health > 0) {

			if (x < 5) {
				x = 5;
			} else if (x > length - this.width) {
				x = length - this.width - 1;
			} else if (y < 5) {
				y = 5;
			} else if (y > height - this.height) {
				y = height - this.height - 1;
			}

			// Temporary Fix where the Unit loses its x and y position.
			// This is most likely due to when they are on top of each other.
			// I am not sure but its either in the vector method.
			else if (x >= 5) {
			} else if (x <= length - this.width) {
			} else if (y >= 5) {
			} else if (y <= height - 100 - this.height) {
			} else {
				// When the x and y are returned as NaN
				x = xstart;
				y = ystart;
			}
		}
	}

}
