import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class Team extends Thread {

	// Total units
	ArrayList<Unit> totalTroops = new ArrayList<Unit>();

	// Collection of all the Enemy Units
	ArrayList<Unit> enemyUnits = new ArrayList<Unit>();

	// Total buildings
	ArrayList<Building> totalBuildings = new ArrayList<Building>();
	int buildingsControlled = 0;

	// Collection of all the Enemy/Empty Buildings
	ArrayList<Building> enemyBuildings = new ArrayList<Building>();

	// team side
	int number;

	int numberOfSoldiersType0 = 0;
	int numberOfSoldiersType1 = 0;
	int numberOfSoldiersType2 = 0;

	float totalDeaths = 0;
	float totalKills = 0;
	float killValue = 0;

	float KD = 0;
	float KV = 0;

	// Population doesn't just equal the totalTroops added up
	int population = 0;
	int populationLimit;
	int totalSpawned = 0;

	// Income
	int initIncome = 100;
	int income = initIncome;
	int incomeRate = 500;
	int timeTilIncome = incomeRate;
	int initIncomeAmount = 10;
	int incomeAmount = initIncomeAmount;
	int maxIncome = populationLimit * initIncomeAmount;

	// Variable for the control set of the builder
	int key0;

	// Debugs for overloaded collections
	int speedup = 0;

	// Velocities
	int xVel = 0;
	int yVel = 0;

	// Double tap timers
	int up = 0;
	int down = 0;
	int right = 0;
	int left = 0;

	// Aura Range of Effect
	int commandAura = 100;
	int doubleTapTime = 10;

	// Builder coordinates
	int xStart = 0;
	int yStart = 0;

	// Builder Width and Height
	int startWidth = 10;
	int startHeight = 10;

	boolean teamHasDied = false;

	// Team Color
	Color teamColor;
	String colorOfTeam;

	// Random generator
	Random r = new Random();

	public void hasDied() {
		teamHasDied = true;
	}

	public void fullReset() {
		reset();
		teamHasDied = false;
		totalSpawned = 0;
	}

	public Team(Color tNumber, int unit0Key, int teamNumber, int popLimit) {
		teamColor = tNumber;
		if (teamColor == Color.RED) {
			colorOfTeam = "Red";
		}
		if (teamColor == Color.GREEN) {
			colorOfTeam = "Green";
		}
		if (teamColor == Color.MAGENTA) {
			colorOfTeam = "Magenta";
		}
		if (teamColor == Color.YELLOW) {
			colorOfTeam = "Yellow";
		}
		key0 = unit0Key;
		number = teamNumber;
		populationLimit = popLimit;
		resetBuilder();
	}

	// Change pop
	public void changePop(int newPopulationLimit) {
		populationLimit = newPopulationLimit;
	}

	// Reset Builder Location
	public void resetBuilder() {
		if (number == 0) {
			xStart = 0;
			yStart = 0;
		} else if (number == 1) {
			xStart = Main.lengthOfFrame - 20;
			yStart = 0;
		} else if (number == 2) {
			xStart = 0;
			yStart = Main.heightOfFrame - StatisticPanel.height - 20;
		} else if (number == 3) {
			xStart = Main.lengthOfFrame - 20;
			yStart = Main.heightOfFrame - StatisticPanel.height - 20;
		}
	}

	// Resets the Team
	public void reset() {
		totalTroops.clear();
		enemyUnits.clear();
		totalBuildings.clear();
		enemyBuildings.clear();

		buildingsControlled = 0;

		numberOfSoldiersType0 = numberOfSoldiersType1 = numberOfSoldiersType2 = 0;
		totalDeaths = totalKills = killValue = 0;
		income = initIncome;
		timeTilIncome = incomeRate;
		incomeAmount = initIncomeAmount;
		maxIncome = populationLimit * initIncomeAmount;
	}

	// Paints everything in the team
	public void paint(Graphics g, ArrayList<Team> t) {

		// Paints Unit
		for (Unit i : totalTroops) {
			if (i != null)
				if (i.health > 0) {
					g.setColor(teamColor);
					g.fillRect((int) i.x, (int) i.y, (int) i.width,
							(int) i.height);

					if (i.type == 1) {
						g.setColor(Color.GRAY);
						g.drawRect((int) i.x, (int) i.y, (int) i.width,
								(int) i.height);
					}
					if (i.type == 2) {
						g.setColor(Color.BLUE);
						g.drawRect((int) i.x, (int) i.y, (int) i.width,
								(int) i.height);
					}
					g.setColor(Color.WHITE);
					g.fillRect((int) i.x, (int) i.y, (int) (i.health
							/ i.initHealth * i.width), (int) i.height / 5);
				}
		}

		for (Building b : totalBuildings) {
			g.setColor(teamColor);
			g.fillRect((int) b.x, (int) b.y, (int) b.width, (int) b.height);

			g.setColor(Color.GRAY);
			g.drawRect((int) b.x, (int) b.y, (int) b.width, (int) b.height);
			g.drawRect((int) b.x + 1, (int) b.y + 1, (int) b.width - 2,
					(int) b.height - 2);

			g.setColor(Color.BLUE);
			g.drawRect((int) b.x - 10, (int) b.y - 10, 10, 10);

			g.setColor(teamColor);
			g.drawArc(b.x + b.width / 2, b.y + b.height / 2, b.width, b.height,
					0, 360);
		}

		g.setColor(teamColor);

		// Drawing Builder
		g.drawRect(xStart, yStart, startWidth, startHeight);
		g.drawArc(xStart, yStart, startWidth, startHeight, 0, 360);
		g.drawArc(xStart - commandAura + startWidth / 2, yStart - commandAura
				+ startHeight / 2, commandAura * 2, commandAura * 2, 0, 360);

		// Drawing statistics
		g.drawString("T0: " + numberOfSoldiersType0, number * 100, 610);
		g.drawString("T1: " + numberOfSoldiersType1, number * 100, 610 + 10);
		g.drawString("T2: " + numberOfSoldiersType2, number * 100, 610 + 20);
		g.drawString("Po: " + population, number * 100, 610 + 30);
		g.drawString("De: " + totalDeaths, number * 100, 610 + 40);
		g.drawString("Ki: " + totalKills, number * 100, 610 + 50);
		g.drawString("KV: " + killValue, number * 100, 610 + 60);
		if (totalDeaths > 0) {
			g.drawString("KD: " + round(KD, 3), number * 100, 610 + 70);
			g.drawString("KV: " + round(KV, 3), number * 100, 610 + 80);
		}

		g.drawString("Money: " + income, 500 + number * 100, 610);
	}

	// Rounding
	public static BigDecimal round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd;
	}

	// Sets which keys are used for the press
	public void keySetPress(KeyEvent e, int up, int down, int left, int right) {
		// Moving builder
		if (e.getKeyCode() == up) {
			yVel = -1;
			if (this.up > 0) {
				for (Unit u : totalTroops) {
					if ((u.x - xStart) * (u.x - xStart) + (u.y - yStart)
							* (u.y - yStart) <= commandAura * commandAura) {
						u.yMinus();
					}
				}

			}
		}
		if (e.getKeyCode() == down) {
			yVel = 1;
			if (this.down > 0) {
				for (Unit u : totalTroops) {
					if ((u.x - xStart) * (u.x - xStart) + (u.y - yStart)
							* (u.y - yStart) <= commandAura * commandAura) {
						u.yPlus();
					}
				}
			}
		}
		if (e.getKeyCode() == left) {
			xVel = -1;
			if (this.left > 0) {
				for (Unit u : totalTroops) {
					if ((u.x - xStart) * (u.x - xStart) + (u.y - yStart)
							* (u.y - yStart) <= commandAura * commandAura) {
						u.xMinus();
					}
				}
			}
		}
		if (e.getKeyCode() == right) {
			xVel = 1;
			if (this.right > 0) {
				for (Unit u : totalTroops) {
					if ((u.x - xStart) * (u.x - xStart) + (u.y - yStart)
							* (u.y - yStart) <= commandAura * commandAura) {
						u.xPlus();
					}
				}
			}
		}

	}

	// Sets which keys are used for the release
	public void keySetRelease(KeyEvent e, int spawn0, int spawn1, int spawn2,
			int spawn3, int up, int down, int left, int right) {
		if (population < populationLimit) {
			// Spawning Unit type 1
			if (e.getKeyCode() == spawn1) {
				spawnUnit(1);
			}
			// Spawning Unit type 2
			if (e.getKeyCode() == spawn2) {
				spawnUnit(2);
			}
			if (e.getKeyCode() == spawn3) {
				spawnUnit(3);
			}
		}

		// Directions
		if (e.getKeyCode() == up) {
			yVel++;
			this.up = doubleTapTime;
		}
		if (e.getKeyCode() == down) {
			yVel--;
			this.down = doubleTapTime;
		}
		if (e.getKeyCode() == left) {
			xVel++;
			this.left = doubleTapTime;
		}
		if (e.getKeyCode() == right) {
			xVel--;
			this.right = doubleTapTime;
		}
	}

	// Keying
	public void keyPr(KeyEvent e) {

		// Set 0
		if (key0 == KeyEvent.VK_1) {
			keySetPress(e, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A,
					KeyEvent.VK_D);
		}

		// Set 1
		if (key0 == KeyEvent.VK_X) {
			keySetPress(e, KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F,
					KeyEvent.VK_H);

		}

		// Set 2
		if (key0 == KeyEvent.VK_SLASH) {
			keySetPress(e, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
					KeyEvent.VK_RIGHT);

		}

		if (key0 == KeyEvent.VK_B) {
			keySetPress(e, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J,
					KeyEvent.VK_L);
		}
	}

	// Spawns the Unit
	public void spawnUnit(int unitType) {

		boolean enoughMoney = false;
		if (unitType == 0) {
			if (income >= 10) {
				enoughMoney = true;
				income -= 10;
			}
		} else if (unitType == 1) {
			if (income >= 20) {
				enoughMoney = true;
				income -= 20;
			}
		} else if (unitType == 2) {
			if (income >= 10) {
				enoughMoney = true;
				income -= 10;
			}
		} else if (unitType == 3) {
			if (income >= 10) {
				enoughMoney = true;
				income -= 10;
			}

		}
		if (enoughMoney) {
			if (teamHasDied) {

			} else {
				Unit u = new Unit(unitType, this, number);
				u.x = xStart;
				u.y = yStart;
				totalTroops.add(u);
				totalSpawned++;
				if (unitType == 0) {
					numberOfSoldiersType0 += 1;
				}
				if (unitType == 1) {
					numberOfSoldiersType1 += 1;
				}
				if (unitType == 2) {
					numberOfSoldiersType2 += 1;
				}
			}
		}

	}

	// Release Keying for the team
	public void keyRe(KeyEvent e, int keyNumber) {
		// Spawning Unit type0
		if (e.getKeyCode() == keyNumber) {
			if (population < populationLimit) {
				spawnUnit(0);
			}
		}

		// Set 0
		if (key0 == KeyEvent.VK_1) {
			keySetRelease(e, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3,
					KeyEvent.VK_4, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A,
					KeyEvent.VK_D);
		}

		// Set 1
		if (key0 == KeyEvent.VK_X) {
			keySetRelease(e, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V,
					KeyEvent.VK_Y, KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F,
					KeyEvent.VK_H);
		}

		// Set 2
		if (key0 == KeyEvent.VK_SLASH) {
			keySetRelease(e, KeyEvent.VK_SLASH, KeyEvent.VK_PERIOD,
					KeyEvent.VK_COMMA, KeyEvent.VK_SEMICOLON, KeyEvent.VK_UP,
					KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
		}

		// Set 3
		if (key0 == KeyEvent.VK_B) {
			keySetRelease(e, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M,
					KeyEvent.VK_O, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J,
					KeyEvent.VK_L);
		}
	}

	// Controls Builder
	public void actionBuilder(ActionEvent e) {
		// Moves Builder
		xStart += xVel;
		yStart += yVel;

		// Restricts Builder
		if (xStart < 5) {
			xStart = 5;
		}
		if (xStart > Main.lengthOfFrame - this.startWidth) {
			xStart = Main.lengthOfFrame - this.startWidth - 1;
		}
		if (yStart < 5) {
			yStart = 5;
		}
		if (yStart > Main.heightOfFrame - StatisticPanel.height
				- this.startHeight) {
			yStart = Main.heightOfFrame - this.startHeight - 1
					- StatisticPanel.height;
		}

		// Tap time
		up--;
		down--;
		left--;
		right--;

		// Finds Population
		population = numberOfSoldiersType0 + numberOfSoldiersType1 * 2
				+ numberOfSoldiersType2 * 1;

	}

	public void actionPer(ActionEvent e, ArrayList<Team> teams,
			ArrayList<Obstruction> obstructions, Structures structure,int timeTilIncome) {
		maxIncome = populationLimit * initIncomeAmount;
		buildingsControlled = 0;
		// Income due to buildings
		for (Building b : structure.totalBuildings) {
			if (b.buildingSide == number) {
				buildingsControlled += 1;
			}
		}

		if (structure.numberOfBuildings == 48) {
			incomeAmount = 10 + buildingsControlled * 10;
		} else if (structure.numberOfBuildings == 5) {
			incomeAmount = 10 + buildingsControlled * 40;
		} else if (structure.numberOfBuildings == 4) {
			incomeAmount = 10 + buildingsControlled * 50;
		} else {
			incomeAmount = 500;
		}

		// Income
		if ((timeTilIncome <= 0) && (income <= maxIncome)) {
			income += incomeAmount;
		}
		System.out.println("Time Til:" + timeTilIncome);

		if (totalDeaths > 0) {
			KD = totalKills / totalDeaths;
			KV = killValue / totalDeaths;
		}

		// Reduces Lag
		speedup++;

		if (speedup > 300) {
			speedup = 0;
			enemyUnits.clear();
		}

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

		// Deletes allied Units that have died
		if (totalTroops.isEmpty()) {
		} else {
			try {

				for (Unit i : totalTroops) {
					if (i != null) {
						if (i.health <= 0) {

							if (i.health != -1) {
								if (i.type == 0) {
									numberOfSoldiersType0--;
								}
								if (i.type == 1) {
									numberOfSoldiersType1--;
								}
								if (i.type == 2) {
									numberOfSoldiersType2--;
								}
								totalDeaths++;
								i.health = -1;
								totalTroops.remove(totalTroops.indexOf(i));

							}
						} else {

							i.start(enemyUnits, totalTroops,
									Main.lengthOfFrame, Main.heightOfFrame
											- StatisticPanel.height, xStart,
									yStart, obstructions);
							i.resetKills();
						}
					} else {
						totalTroops.remove(totalTroops.indexOf(i));
					}
				}
			} catch (Exception e1) {
			}
		}

		// Deletes enemy Units that have died
		if (enemyUnits.isEmpty()) {

		} else {
			try {
				for (Unit i : enemyUnits) {
					if (i != null) {
						if (i.health <= 0) {
							if ((i.distanceFromUnit(i.nearestEnemy(totalTroops)) < i
									.distanceFromUnit(i
											.nearestEnemy(enemyUnits)))) {

							}

							if (i.killer == number) {
								totalKills++;
								if ((i.type == 0) || (i.type == 2)) {
									killValue += 1;
								} else if (i.type == 1) {
									killValue += 2;
								}
							}
							enemyUnits.remove(enemyUnits.indexOf(i));
						}
					} else {
						enemyUnits.remove(enemyUnits.indexOf(i));
					}
				}
			} catch (Exception e1) {
			}
		}

		// Buildings with income

		// Deletes allied Buildings that have been lost
		if (totalBuildings.isEmpty()) {
		} else {
			try {
				for (Building b : totalBuildings) {
					if (b.buildingSide != number) {
						totalBuildings.remove(totalBuildings.indexOf(b));
					}
				}
			} catch (Exception e1) {
			}
		}

		// Adds allied Buildings that have been gained
		if (enemyBuildings.isEmpty()) {
		} else {
			try {
				for (Building b : enemyBuildings) {
					if (b.buildingSide == number) {
						totalBuildings.add(b);
						enemyBuildings.remove(enemyBuildings.indexOf(b));
					}
				}
			} catch (Exception e1) {
			}
		}
	}

}