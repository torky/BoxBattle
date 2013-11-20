import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class AI48 extends Team {
	
	boolean didNotConquer = true;

	public AI48(Color tNumber, int unit0Key, int teamNumber, int popLimit) {
		super(tNumber, unit0Key, teamNumber, popLimit);
	}

	public void actionAI48(ActionEvent e, ArrayList<Team> teams,
			ArrayList<Obstruction> obstructions, Structures structure) {
		xStart += r.nextInt(2);
		yStart += r.nextInt(2);
		xStart -= r.nextInt(2);
		yStart -= r.nextInt(2);
		Team target = overPoweredTeam(teams);
		
		if(target == null){
			
		}else{
			Unit u = nearestUnit(target.totalTroops);
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
					direct(u.x, u.y, 3, "movement");
				}
			}
			for(Team t: teams){
				if((t!=this)&&(t!=target)){
					int distance = ((xStart - t.xStart) * (xStart - t.xStart) + (yStart - t.yStart) * (yStart - t.yStart));
					int totalMoved = 500;
					int moveDistance = r.nextInt(totalMoved);
					if(distance<100*100){
						if(r.nextBoolean()){
							xStart+=moveDistance;
							totalMoved-=moveDistance;
						}else{
							xStart-=moveDistance;
							totalMoved-=moveDistance;
						}
						if(r.nextBoolean()){
							yStart+=totalMoved;
							totalMoved-=totalMoved;
						}else{
							yStart-=totalMoved;
							totalMoved-=totalMoved;
						}
					}
				}
			}
		}

		Building b = searchForBuilding(structure.totalBuildings);
		if (b != null) {
			if (distanceFromBuilding(b) < 10 * 10) {
				if (didNotConquer) {
					spawnUnit(3);
					didNotConquer = false;
				}
			}else{
				direct(b.x, b.y, 2, "movement");
				didNotConquer = true;
			}
		} else {
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
	
	public Team overPoweredTeam(ArrayList<Team> teams){
		Team teamWinning = null;
		for(Team t: teams){
			if(t!=this){
				if(t.buildingsControlled>10){
					if(teamWinning == null){
						teamWinning = t;
					} else if(t.buildingsControlled>teamWinning.buildingsControlled){
						teamWinning = t;
					}
				}
			}
		}
		return teamWinning;
	}

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
