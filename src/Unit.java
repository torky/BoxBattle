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

	Unit enemyUnit;

	Player player;

	Random r = new Random();

	boolean attack = false;

	// Initializing the Unit and choosing its qualities
	public Unit(int kind, Player player, int whichSide) {
		type = kind;
		side = whichSide;
		notDefending = true;
		// Fast Unit
		if (type == 0) {
			width = 10;
			height = 10;
			speed = .5F;
			initSpeed = .5F;
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
			range = 350;
			reload = 0;
			reloadTime = 100;
			boing = 30;
			soldierPop = 1;
		}
		// Wall Unit
		else if (type == 3) {
			width = 30;
			height = 30;
			speed = 0f;
			initSpeed = 0f;
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
	public void start(ArrayList<Unit> u, ArrayList<Unit> ally,
			ArrayList<Unit> target, int xlength, int yheight, int xstart,
			int ystart, ArrayList<Obstruction> obstructions,ArrayList<Unit> otherAllies) {
		for (Obstruction o : obstructions) {
			obstruct(o);
		}

		targetEnemy(target, ally);

		if ((nearestEnemy(u) != null) && (this != null)) {
			collision(u);
			collision(ally);
			collision(otherAllies);
			if (health > 0) {
				beingDamaged(u);
			}
		}

		if (enemyUnit != null) {
			if (notDefending) {
				move(enemyUnit);
			}
		}

		attack(enemyUnit);

		reload();
		restrict(xlength, yheight, xstart, ystart);

	}

	public void targetEnemy(ArrayList<Unit> enemyUnitList,
			ArrayList<Unit> allyUnitList) {
		if (enemyUnitList.isEmpty()) {

		} else {
			if (enemyUnit == null) {
				enemyUnit = nearestEnemy(enemyUnitList);
			} else if (enemyUnit.health <= 0) {
				enemyUnit = nearestEnemy(enemyUnitList);
			} else if ((distanceFromUnit(enemyUnit) > 20 * 20)) {
				if (collision(enemyUnitList)) {
					enemyUnit = nearestEnemy(enemyUnitList);
				}
				if (collision(allyUnitList)) {
					enemyUnit = nearestEnemy(enemyUnitList);
				}
			}
		}
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
			if (x >= xOther) {
				x += boingFactor
						* vectorSpeed('x', speed, x, y, xOther, yOther);
			} else if (x <= xOther) {
				x -= boingFactor
						* vectorSpeed('x', speed, x, y, xOther, yOther);
			}
			if (y >= yOther) {
				y += boingFactor
						* vectorSpeed('y', speed, x, y, xOther, yOther);
			} else if (y <= yOther) {
				y -= boingFactor
						* vectorSpeed('y', speed, x, y, xOther, yOther);
			}
		} else if (type == "obstruct") {
			if (x + this.width / 2 >= xOther + width / 2) {
				x += boingFactor
						* vectorSpeed('a', speed, x, y, xOther, yOther);
			} else if (x + this.width / 2 <= xOther + width / 2) {
				x -= boingFactor
						* vectorSpeed('a', speed, x, y, xOther, yOther);
			}
			if (y + this.height / 2 >= yOther + height / 2) {
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
	public boolean collision(ArrayList<Unit> arrayList) {
		boolean collides = false;
		for (Unit mob : arrayList) {
			if ((mob != null) && (mob != this)) {
				if ((mob.health > 0)) {
					if ((y <= mob.y + mob.height && y >= mob.y - height)
							&& (x <= mob.x + mob.width && x >= mob.x - width)) {
						direct(mob.x, mob.y, mob.width, mob.height, 1,
								"movement");
						collides = true;
					}
				}
			}
		}
		return collides;
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
		if ((u != null) && (u.health > 0)) {
			distance = ((getCenterX() - u.getCenterX()) * (getCenterX() - u.getCenterX()) + (getCenterY() - u.getCenterY()) * (getCenterY() - u.getCenterY()));
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
	
	public float getCenterX(){
		float centerX = x+width/2;
		return centerX;
	}
	
	public float getCenterY(){
		float centerY = y+height/2;
		return centerY;
	}
	
	//teleport
	public void setX(float xPosition){
		x = xPosition;
	}
	
	public void setY(float yPosition){
		y = yPosition;
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
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}


	// This method deals the specified damage to the Unit itself
	public void beingDamaged(ArrayList<Unit> mobArray) {
		for (Unit mob : mobArray) {
			if (mob != null)
				if ((mob.attack(this))) {
					if((mob.damage - armor - augmentPower)>0){
						health -= (mob.damage - armor - augmentPower);
					}
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

	public void reAim() {
		enemyUnit = null;
	}
	

}
