import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class Player {

	// Total units
	ArrayList<Unit> totalTroops = new ArrayList<Unit>();
	ArrayList<Unit> allyTroops = new ArrayList<Unit>();
	ArrayList<Player> allPlayers = new ArrayList<Player>();

	// Collection of all the Enemy Units
	ArrayList<Unit> enemyUnits = new ArrayList<Unit>();

	// Collection of what Enemies you target
	ArrayList<Unit> targetEnemyUnits = new ArrayList<Unit>();

	// Total buildings
	ArrayList<Building> totalBuildings = new ArrayList<Building>();
	int buildingsControlled = 0;

	// Collection of all the Enemy/Empty Buildings
	ArrayList<Building> enemyBuildings = new ArrayList<Building>();

	// player side
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
	int speed = 1;

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

	boolean playerHasDied = false;

	// Player Color
	Color playerColor;
	String colorOfPlayer;

	// Allies
	int allyPlayerNumber;
	boolean areAllies = false;

	// Random generator
	Random r = new Random();

	public void hasDied() {
		playerHasDied = true;
	}

	public void fullReset() {
		reset();
		playerHasDied = false;
		totalSpawned = 0;
	}

	public Player(Color tNumber, int unit0Key, int playerNumber, int popLimit,
			boolean teams) {
		playerColor = tNumber;
		allyPlayerNumber = playerNumber;
		if (playerColor == Color.RED) {
			colorOfPlayer = "Red";
		}
		if (playerColor == Color.GREEN) {
			colorOfPlayer = "Green";
		}
		if (playerColor == Color.MAGENTA) {
			colorOfPlayer = "Magenta";
		}
		if (playerColor == Color.YELLOW) {
			colorOfPlayer = "Yellow";
		}
		key0 = unit0Key;
		number = playerNumber;
		populationLimit = popLimit;
		resetBuilder();
		areAllies = teams;
	}

	public void setAllies(int Allies) {
		allyPlayerNumber = Allies;
		areAllies = true;
	}

	public Player(Color tNumber, int unit0Key, int teamNumber, int popLimit,
			int Allies, ArrayList<Player> allPlayers) {
		Allies = allyPlayerNumber;
		playerColor = tNumber;
		if (playerColor == Color.RED) {
			colorOfPlayer = "Red";
		}
		if (playerColor == Color.BLUE) {
			colorOfPlayer = "BLUE";
		}
		if (playerColor == Color.MAGENTA) {
			colorOfPlayer = "Magenta";
		}
		if (playerColor == Color.YELLOW) {
			colorOfPlayer = "Yellow";
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

	// Resets the Player
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

	// Paints everything in the player
	public void paint(Graphics g, ArrayList<Player> t) {

		// Paints Unit
		for (Unit i : totalTroops) {
			if (i != null)
				if (i.health > 0) {
					g.setColor(playerColor);
					g.fillRect((int) i.x, (int) i.y, (int) i.width,
							(int) i.height);

					if (i.type == 1) {
						g.setColor(Color.GRAY);
						g.drawRect((int) i.x, (int) i.y, (int) i.width,
								(int) i.height);
					}
					if (i.type == 2) {
						g.setColor(Color.WHITE);
						g.drawRect((int) i.x, (int) i.y, (int) i.width,
								(int) i.height);
					}
					g.setColor(Color.GREEN);
					g.fillRect((int) i.x, (int) i.y, (int) (i.health
							/ i.initHealth * i.width), (int) i.height / 5);
				}
		}

		// Don't think this code is being used
		for (Building b : totalBuildings) {
			g.setColor(playerColor);
			g.fillRect((int) b.x, (int) b.y, (int) b.width, (int) b.height);

			g.setColor(Color.GRAY);
			g.drawRect((int) b.x, (int) b.y, (int) b.width, (int) b.height);
			g.drawRect((int) b.x + 1, (int) b.y + 1, (int) b.width - 2,
					(int) b.height - 2);

			g.setColor(Color.BLUE);
			g.drawRect((int) b.x - 10, (int) b.y - 10, 10, 10);

			g.setColor(playerColor);
			g.drawArc(b.x + b.width / 2, b.y + b.height / 2, b.width, b.height,
					0, 360);
		}

		g.setColor(playerColor);

		// Drawing Cursor
		g.drawRect(xStart, yStart, startWidth, startHeight);
		g.drawArc(xStart, yStart, startWidth, startHeight, 0, 360);
		g.drawArc(xStart - commandAura + startWidth / 2, yStart - commandAura
				+ startHeight / 2, commandAura * 2, commandAura * 2, 0, 360);

		// Drawing Statistics
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

		// Writing Amount of Money
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
			yVel = -speed;
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
			yVel = speed;
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
			xVel = -speed;
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
			xVel = speed;
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
			// Spawning the Strong Unit
			if (e.getKeyCode() == spawn1) {
				spawnUnit(1);
			}
			// Spawning the Ranged Unit
			if (e.getKeyCode() == spawn2) {
				spawnUnit(2);
			}
			// Spawning the Wall Unit
			if (e.getKeyCode() == spawn3) {
				spawnUnit(3);
			}
		}

		// Double tapping to move the units within the circle in a direction
		// moved towards
		if (e.getKeyCode() == up) {
			yVel += speed;
			this.up = doubleTapTime;
		}
		if (e.getKeyCode() == down) {
			yVel -= speed;
			this.down = doubleTapTime;
		}
		if (e.getKeyCode() == left) {
			xVel += speed;
			this.left = doubleTapTime;
		}
		if (e.getKeyCode() == right) {
			xVel -= speed;
			this.right = doubleTapTime;
		}
	}

	// Setting up the key signature
	public void keyPr(KeyEvent e) {

		// Set 0
		if (key0 == KeyEvent.VK_Z) {
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
			if (playerHasDied) {

			} else {
				Unit u = new Unit(unitType, this, number);
				u.x = xStart;
				u.y = yStart;
				totalTroops.add(u);
				// Adding total spawned
				totalSpawned++;
				if (unitType == 0) {
					// Counting the number of fast units
					numberOfSoldiersType0 += 1;
				}
				if (unitType == 1) {
					// Counting the number of strong units
					numberOfSoldiersType1 += 1;
				}
				if (unitType == 2) {
					// Counting the number of ranged units
					numberOfSoldiersType2 += 1;
				}
			}
		}

	}

	// Release Keying for the player
	public void keyRe(KeyEvent e, int keyNumber) {
		// Spawning Unit fast
		if (e.getKeyCode() == keyNumber) {
			if (population < populationLimit) {
				spawnUnit(0);
			}
		}

		// Set 0
		if (key0 == KeyEvent.VK_Z) {
			keySetRelease(e, KeyEvent.VK_Z, KeyEvent.VK_E, KeyEvent.VK_R,
					KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A,
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

	public void addEnemies(ArrayList<Player> players) {
		for (Player t : players) {
			if ((t != this)) {
				if ((t.allyPlayerNumber != allyPlayerNumber)) {
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
	}

	public void addAllies(ArrayList<Player> players) {
		for (Player t : players) {
			if ((t != this)) {
				if ((t.allyPlayerNumber == allyPlayerNumber)) {
					// Adds Enemy Units to enemyUnits Collection
					if (allyTroops.containsAll(t.totalTroops)) {
					} else {
						allyTroops.removeAll(t.totalTroops);
						allyTroops.addAll(t.totalTroops);
					}
				}
			}
		}
	}

	// Action depending on the game being paused
	public void actionPer(ActionEvent e, ArrayList<Player> players,
			ArrayList<Obstruction> obstructions, Structures structure,
			int timeTilIncome) {
		maxIncome = populationLimit * initIncomeAmount;
		buildingsControlled = 0;
		// Income due to buildings
		for (Building b : structure.totalBuildings) {
			if (b.buildingSide == number) {
				buildingsControlled += 1;
			}
		}

		setTargetEnemyUnits();

		// Adding income based on the game type
		if (structure.numberOfBuildings == 48) {
			incomeAmount = 10 + buildingsControlled * 10;
		} else if (structure.numberOfBuildings == 5) {
			incomeAmount = 10 + buildingsControlled * 40;
		} else if (structure.numberOfBuildings == 4) {
			incomeAmount = 10 + buildingsControlled * 50;
		} else {
			incomeAmount = 1000;
		}

		// Adding the income at intervals
		if ((timeTilIncome <= 0) && (income <= maxIncome)) {
			income += incomeAmount;
		}

		// Calculating KD and KV
		if (totalDeaths > 0) {
			KD = totalKills / totalDeaths;
			KV = killValue / totalDeaths;
		}

		// Reduces Lag
		speedup++;

		if (speedup > 100) {
			speedup = 0;
			enemyUnits.clear();
		}

		addEnemies(players);
		if (areAllies) {
			addAllies(players);
		}

		dealWithUnitCollections(obstructions);

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

	public void dealWithAllies(ArrayList<Player> players) {
		for (Player t : players) {
			if (t.allyPlayerNumber == allyPlayerNumber) {
				if (allyTroops.containsAll(t.totalTroops)) {
				} else {
					allyTroops.removeAll(t.totalTroops);
					allyTroops.addAll(t.totalTroops);
				}
			}
		}
	}

	public void dealWithUnitCollections(ArrayList<Obstruction> obstructions) {
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

							i.start(enemyUnits, totalTroops, targetEnemyUnits,
									Main.lengthOfFrame, Main.heightOfFrame
											- StatisticPanel.height, xStart,
									yStart, obstructions, allyTroops);
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

							if ((i.killer == number) && (i.notReturnedKills)) {
								totalKills++;
								i.returnKill();
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
	}

	public void setTargetEnemyUnits() {
		targetEnemyUnits = enemyUnits;
	}

}
