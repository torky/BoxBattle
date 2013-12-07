import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class AI48 extends Team {

	boolean didNotConquer = true;
	boolean AILastDitch = false;
	boolean changedAttackPattern = false;

	public AI48(Color tNumber, int unit0Key, int teamNumber, int popLimit) {
		super(tNumber, unit0Key, teamNumber, popLimit);
	}

	Team target;
	
	public void addEnemies(ArrayList<Team> teams) {

		for (Team t : teams) {
			if ((t != this)) {
				// Adds Enemy Units to enemyUnits Collection
				if (enemyUnits.containsAll(t.totalTroops)) {
				} else {
					enemyUnits.removeAll(t.totalTroops);
					enemyUnits.addAll(t.totalTroops);
				}
				// Adds Enemy Buildings to enemyBuildings Collection
				if (enemyBuildings.containsAll(t.enemyBuildings)) {
				} else {
					enemyBuildings.removeAll(t.enemyBuildings);
					enemyBuildings.addAll(t.enemyBuildings);
				}
			}
		}
	}

	public void actionAI48(ActionEvent e, ArrayList<Team> teams,
			ArrayList<Obstruction> obstructions, Structures structure) {
		// Counter Clumping and Units not staying at the cursor
		xStart += r.nextInt(2);
		yStart += r.nextInt(2);
		xStart -= r.nextInt(2);
		yStart -= r.nextInt(2);

		// Choosing to fight team with most money
		target = overPoweredTeam(teams);

		if (target == null) {
			AILastDitch = false;
			changedAttackPattern = false;
		} else {
			AILastDitch = true;

			for (Unit u2 : totalTroops) {
				u2.reAim();
				u2.direct((float) xStart, (float) yStart, 0, 0,
						(int) u2.speed * 2, "movement");
			}

			Unit u = nearestUnit(target.totalTroops);
			if (u == null) {
				// Counter crashes (I think)
				spawnUnit(0);
			} else {
				// Spawning units once it has arrived at its destination
				if (distanceFromUnit(u) < 10 * 10) {
					if (population < populationLimit) {
						spawnUnit(0);
						spawnUnit(1);
						spawnUnit(2);
					}
				} else {
					// Moving toward the target enemy
					direct(u.x, u.y, 4, "movement");
				}
			}
			if (changedAttackPattern) {
				for (Unit u1 : totalTroops) {
					setTargetEnemyUnits();
					u1.reAim();
				}
			}
			changedAttackPattern = true;
			// Moving away if there is a team that is too close that is not the
			// target team.
			for (Team t : teams) {
				if ((t != this) && (t != target)) {
					int distance = ((xStart - t.xStart) * (xStart - t.xStart) + (yStart - t.yStart)
							* (yStart - t.yStart));
					int totalMoved = 500;
					int moveDistance = r.nextInt(totalMoved);
					if (distance < 100 * 100) {
						if (r.nextBoolean()) {
							xStart += moveDistance;
							totalMoved -= moveDistance;
						} else {
							xStart -= moveDistance;
							totalMoved -= moveDistance;
						}
						if (r.nextBoolean()) {
							yStart += totalMoved;
							totalMoved -= totalMoved;
						} else {
							yStart -= totalMoved;
							totalMoved -= totalMoved;
						}
						teleportArmy();
					}
				}
			}
		}

		// Targeting and Capturing Unguarded Buildings
		Building b = searchForBuilding(structure.totalBuildings);
		if (b != null) {
			if (distanceFromBuilding(b) < 10 * 10) {
				if (didNotConquer) {
					spawnUnit(3);
					didNotConquer = false;
				}
			} else {
				direct(b.x, b.y, 2, "movement");
				didNotConquer = true;
			}
		} else {
			// When all the bases are captured, find nearest allied unit and
			// spawn
			Unit u = nearestUnit(totalTroops);
			if (u == null) {
				spawnUnit(0);
			} else {
				if (distanceFromUnit(u) < 10 * 10) {
					if (population < populationLimit) {
						spawnUnit(0);
						spawnUnit(1);
						spawnUnit(2);
					}
				} else {
					direct(u.x, u.y, 2, "movement");
				}
			}
		}

	}

	// Move cursor to point
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

	// Finds nearest Unit
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

	// Finds nearest empty building
	public Building searchForBuilding(ArrayList<Building> buildings) {
		Building closestBuilding = null;
		for (Building b : buildings) {
			if ((b.defender == null)) {
				if (closestBuilding == null) {
					closestBuilding = b;
				} else if (distanceFromBuilding(b) < distanceFromBuilding(closestBuilding)) {
					closestBuilding = b;
				}
			} else if (closestBuilding != null) {
				if (closestBuilding.defender != null) {
					closestBuilding = null;
				}
			}

		}
		return closestBuilding;
	}

	// Finds the Winning Team
	public Team overPoweredTeam(ArrayList<Team> teams) {
		Team teamWinning = null;
		for (Team t : teams) {
			if (t != this) {
				if (t.buildingsControlled > 15) {
					if (teamWinning == null) {
						teamWinning = t;
					} else if (t.buildingsControlled > teamWinning.buildingsControlled) {
						teamWinning = t;
					}
				}
			}
		}
		return teamWinning;
	}

	// Finds distance or returns an extreme distance from a unit if there is an
	// error.
	public float distanceFromUnit(Unit u) {
		float distance = 10000;

		if ((u != null) && (u.health > 0)) {
			distance = ((xStart - u.x) * (xStart - u.x) + (yStart - u.y)
					* (yStart - u.y));
			/*
			 * if (((xStart - u.x - u.width) * (xStart - u.x - u.width) +
			 * (yStart - u.y) (yStart - u.y)) < distance) { distance = ((xStart
			 * - u.x - u.width) * (xStart - u.x) + (yStart - u.y) (yStart -
			 * u.y)); }
			 */
			if (distanceSquared(xStart, yStart, u.x, u.y, u.width, 0) < distance) {
				distance = distanceSquared(xStart, yStart, u.x, u.y, u.width, 0);
			}
			/*
			 * if (((xStart - u.x) * (xStart - u.x) + (yStart - u.y - u.height)
			 * (yStart - u.y - u.height)) < distance) { distance = ((xStart -
			 * u.x) * (xStart - u.x) + (yStart - u.y - u.height) (yStart - u.y -
			 * u.height)); }
			 */
			if (distanceSquared(xStart, yStart, u.x, u.y, 0, u.height) < distance) {
				distance = distanceSquared(xStart, yStart, u.x, u.y, 0,
						u.height);
			}
			/*
			 * if (((xStart - u.x - u.width) * (xStart - u.x - u.width) +
			 * (yStart - u.y) (yStart - u.y)) < distance) { distance = ((xStart
			 * - u.x - u.width) * (xStart - u.x - u.width) + (yStart - u.y)
			 * (yStart - u.y)); }
			 */
			if (distanceSquared(xStart, yStart, u.x, u.y, 0, u.height) < distance) {
				distance = distanceSquared(xStart, yStart, u.x, u.y, u.width,
						u.height);
			}
			return distance;
		} else {
			distance = 10000;
			return distance;
		}
	}

	public float distanceSquared(float x1, float y1, float x2, float y2,
			float width, float height) {
		float distanceSquared = ((x1 - x2 - width) * (x1 - x2 - width) + (y1
				- y2 - height)
				* (y1 - y2 - height));
		return distanceSquared;
	}

	// Finds distance or returns an extreme distance from a Building if there is
	// an error.
	public float distanceFromBuilding(Building b) {
		float distance = 10000;

		if ((b != null)) {
			distance = ((xStart - b.x) * (xStart - b.x) + (yStart - b.y)
					* (yStart - b.y));
			if (((xStart - b.x - b.width) * (xStart - b.x - b.width) + (yStart - b.y)
					* (yStart - b.y)) < distance) {
				distance = ((xStart - b.x - b.width) * (xStart - b.x) + (yStart - b.y)
						* (yStart - b.y));
			}
			if (((xStart - b.x) * (xStart - b.x) + (yStart - b.y - b.height)
					* (yStart - b.y - b.height)) < distance) {
				distance = ((xStart - b.x) * (xStart - b.x) + (yStart - b.y - b.height)
						* (yStart - b.y - b.height));
			}
			if (((xStart - b.x - b.width) * (xStart - b.x - b.width) + (yStart - b.y)
					* (yStart - b.y)) < distance) {
				distance = ((xStart - b.x - b.width) * (xStart - b.x - b.width) + (yStart - b.y)
						* (yStart - b.y));
			}
			return distance;
		} else {
			distance = 10000;
			return distance;
		}
	}

	// Speed of something
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

	public void teleportArmy(){
		for(Unit u: totalTroops){
			u.setX(xStart);
			u.setY(yStart);
		}
	}
	
	public void setTargetEnemyUnits() {
		if (AILastDitch) {
			// Adds Enemy Units to enemyUnits Collection
			try {
				if (targetEnemyUnits.containsAll(target.totalTroops)) {
				} else {
					targetEnemyUnits.removeAll(target.totalTroops);
					targetEnemyUnits.addAll(target.totalTroops);
					System.out.println("cleaning");

				}
			} catch (Exception e) {

			}
		} else {
			targetEnemyUnits = enemyUnits;
		}
	}
}
