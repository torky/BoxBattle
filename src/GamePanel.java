import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener,
		MouseListener, MouseMotionListener {

	Timer tm = new Timer(50, this);
	boolean hasNotClicked = true;
	String clickString = "Click to Begin";
	
	{
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setSize(Main.lengthOfFrame, Main.heightOfFrame);
		setBackground(Color.BLACK);
		tm.start();
	}
	
	// Initialization
	public GamePanel() {
		quickGame();
		pickAI(0, false, 1, 2, 3, 4);
		playerNumber.add(player1);
		playerNumber.add(player2);
		playerNumber.add(player3);
		playerNumber.add(player4);
		System.out.println("Quick Game");


	}
	
	public GamePanel(boolean kill, boolean spawn, boolean death,
			int killNumber, int spawnNumber, int deathNumber, int aiNumber,
			int populationLimit, String gameTypeStr) {
		pickGame(kill, spawn, death, killNumber, spawnNumber, deathNumber,
				aiNumber, populationLimit, gameTypeStr);
		pickAI(aiNumber, false, 1, 2, 3, 4);
		playerNumber.add(player1);
		playerNumber.add(player2);
		playerNumber.add(player3);
		playerNumber.add(player4);
		System.out.println("Standard Game");
	}

	public GamePanel(boolean kill, boolean spawn, boolean death,
			int killNumber, int spawnNumber, int deathNumber, int aiNumber,
			int populationLimit, String gameTypeStr, boolean teams, int player1N,
			int player2N, int player3N, int player4N) {
		
		pickGame(kill, spawn, death, killNumber, spawnNumber, deathNumber,
				aiNumber, populationLimit, gameTypeStr);
		pickAI(aiNumber, teams, player1N, player2N, player3N, player4N);
		playerNumber.add(player1);
		playerNumber.add(player2);
		playerNumber.add(player3);
		playerNumber.add(player4);
		System.out.println("Team Game");

	}
	
	BufferedImage img = null;
	
	{
		try {
			img = ImageIO.read(new File("Field.jpg"));
		} catch (IOException e) {
			System.out.println("Field image not found.");
		}
	}
	
	boolean didNotStart = true;

	Terrain terrain = new Terrain();
	ArrayList<Player> playerNumber = new ArrayList<Player>();
	ArrayList<AI> AINumber = new ArrayList<AI>();
	ArrayList<AI48> AI48Number = new ArrayList<AI48>();
	ArrayList<Unit> allTroops = new ArrayList<Unit>();

	private static final Random RANDOM = new Random();

	// Game conditions
	// Kills
	boolean kill = false;
	int killLimit = 0;

	// Deaths
	boolean death = false;
	int deathLimit = 0;

	// Spawned
	boolean spawn = false;
	int spawnLimit = 0;

	int gameType = 48;
	int timeTilIncome = 0;
	int incomeRate = 400;

	int popLimiter;
	String pop;
	
	Structures structures;
	
	
	// Picking game conditions
	public void pickGame(boolean kill, boolean spawn, boolean death,
			int killNumber, int spawnNumber, int deathNumber, int aiNumber,
			int populationLimit, String gameTypeStr) {
		// Kill limit or not
		if (kill) {
			killLimit = killNumber;
		}

		// Death limit or not
		if (death) {
			deathLimit = deathNumber;
		}

		// Spawn limit or not
		if (spawn) {
			spawnLimit = spawnNumber;
		}

		gameType = 0;
		
		try {
			gameType = Integer.parseInt(gameTypeStr);
		} catch (NumberFormatException nfe) {
			gameType = 0;
		}
		
		popLimiter = populationLimit;

	}

	public void quickGame() {
		// Kill limit or not
		kill = true;
		killLimit = 500;
		// Death limit or not
		death = false;
		// Spawn limit or not
		spawn = false;
		popLimiter = 500;

		gameType = 0;
	}



	// Images (Don't know why, but I can't seem to load an image)
	ImageIcon get = new ImageIcon();

	// there has to be at least 1 human player
	Player player1;
	Player player2;
	Player player3;
	Player player4;

	int areTeams;

	// Choosing the number of AIs
	public void pickAI(int AITotal, boolean teams, int player1N, int player2N,
			int player3N, int player4N) {
		structures = new Structures(gameType);
		player1 = new Player(Color.RED, KeyEvent.VK_Z, 0, popLimiter,teams);
		if (gameType == 48) {
			int AI48 = AITotal;
			if (AI48 == 1) {
				player2 = new AI48(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new Player(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new Player(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);

				AI48Number.add((AI48) player2);

			} else if (AI48 == 2) {
				player2 = new AI48(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new AI48(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new Player(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);

				AI48Number.add((AI48) player2);
				AI48Number.add((AI48) player3);

			} else if (AI48 == 3) {
				player2 = new AI48(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new AI48(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new AI48(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);

				AI48Number.add((AI48) player2);
				AI48Number.add((AI48) player3);
				AI48Number.add((AI48) player4);

			} else {
				player2 = new Player(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new Player(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new Player(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);
			}
		} else {
			int AI = AITotal;
			if (AI == 1) {
				player2 = new AI(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new Player(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new Player(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);

				AINumber.add((AI) player2);

			} else if (AI == 2) {
				player2 = new AI(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new AI(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new Player(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);

				AINumber.add((AI) player2);
				AINumber.add((AI) player3);

			} else if (AI == 3) {
				player2 = new AI(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new AI(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new AI(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);

				AINumber.add((AI) player2);
				AINumber.add((AI) player3);
				AINumber.add((AI) player4);

			} else {
				player2 = new Player(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter,teams);
				player3 = new Player(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter,teams);
				player4 = new Player(Color.BLUE, KeyEvent.VK_SLASH, 3, popLimiter,teams);
			}
		}

		if (teams) {
			player1.setAllies(player1N);
			player2.setAllies(player2N);
			player3.setAllies(player3N);
			player4.setAllies(player4N);
			System.out.println("set up Teams"+player1N+player2N+player3N+player4N);
		}


	}

	//Mouse Click Values
	int mouseX = 0;
	int mouseY = 0;
	//Box Top Left Corner Values
	int boxX = 0;
	int boxY = 0;
	//Misleading, but this creates the box's width
	int mouseWidth = 0;
	int mouseHeight = 0;
	
	boolean firstClick = true;
	boolean pause = false;

	// ======================================PAINT-COMPONENT==========================================================//
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(img, 0, 0, 1200, 720, this);
		g.setColor(Color.BLACK);
		g.fillRect(0, Main.heightOfFrame - StatisticPanel.height,
				Main.lengthOfFrame, 100);
		g.setColor(Color.WHITE);
		g.drawString("Running: " + pause, 900, 610);
		g.drawRect(boxX, boxY, mouseWidth, mouseHeight);
		g.drawString("Kill Limit: " + killLimit, 900, 610 + 12);
		g.drawString("Death Limit: " + deathLimit, 900, 610 + 24);
		g.drawString("Spawn Limit: " + spawnLimit, 900, 610 + 36);
		g.drawString("Population Limit: " + popLimiter, 900, 610 + 48);
		g.drawString("Time Til Salary: " + timeTilIncome, 900, 610 + 60);
		if(hasNotClicked){
			g.drawString(clickString, Main.lengthOfFrame/2-clickString.length()*3,(Main.heightOfFrame-StatisticPanel.height)/2);
		}
		structures.paint(g);
		terrain.draw(g);
		for (Player player : playerNumber) {
			player.paint(g, playerNumber);
		}

	}

	// Full reset, but doesn't reset cursor location (I have the code already
	// prepared, but I'm just not using it)
	public void resetEverything() {
		System.out.println("resetstart");
		for (Player t : playerNumber) {
			t.fullReset();
		}
		structures.reset();
		System.out.println("resetend");

		terrain.reset();
		int reset = JOptionPane.showConfirmDialog(null, "Reset rules?",
				"Reset", JOptionPane.YES_NO_OPTION);

		if (reset == JOptionPane.YES_OPTION) {
			// Kill limit or not
			int testkill = JOptionPane.showConfirmDialog(null,
					"Do you want a kill number?", "Reset",
					JOptionPane.YES_NO_OPTION);
			if (testkill == JOptionPane.YES_OPTION) {
				killLimit = Integer.parseInt(JOptionPane
						.showInputDialog("Pick the kill number."));
			}

			// Death limit or not
			int testdeath = JOptionPane.showConfirmDialog(null,
					"Do you want a death limit?", "Reset",
					JOptionPane.YES_NO_OPTION);
			if (testdeath == JOptionPane.YES_OPTION) {
				deathLimit = Integer.parseInt(JOptionPane
						.showInputDialog("Pick the death limit."));
			}

			// Spawn limit or not
			int testspawn = JOptionPane.showConfirmDialog(null,
					"Do you want a spawn limit?", "Reset",
					JOptionPane.YES_NO_OPTION);

			if (testspawn == JOptionPane.YES_OPTION) {
				spawnLimit = Integer.parseInt(JOptionPane
						.showInputDialog("Pick the spawn limit."));
			}

			pop = JOptionPane.showInputDialog("Set the Maximum Population");
			popLimiter = Integer.parseInt(pop);
			for (Player t : playerNumber) {
				t.changePop(popLimiter);
			}

			gameType = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the game type 4 or 5 or 48."));
		}
	}

	// Moving and spawning for player Builders
	public void keyPressed(KeyEvent e) {
		for (Player t : playerNumber) {
			boolean canType = true;
			if (spawn) {
				if (t.totalSpawned > spawnLimit) {
					canType = false;
				}
			}
			if (canType) {
				t.keyPr(e);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		// Moving and spawning for player Builders
		for (Player t : playerNumber) {
			boolean canType = true;
			if (spawn) {
				if (t.totalSpawned > spawnLimit) {
					canType = false;
				}
			}
			if ((canType) && (t.playerHasDied == false)) {
				t.keyRe(e, t.key0);

			}
		}

		// Reset button (doesn't reset Builder locations)
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			pause = false;

			int fullReset = -1;
			fullReset = JOptionPane.showConfirmDialog(null,
					"Do you want a full reset?", "Reset",
					JOptionPane.YES_NO_OPTION);
			if (fullReset == JOptionPane.NO_OPTION) {

				int i = 1;
				int answer = -1;
				answer = JOptionPane.showConfirmDialog(null,
						"Do you want to reset troops?", "Reset", i,
						JOptionPane.YES_NO_OPTION);
				int nextAnswer = -1;
				nextAnswer = JOptionPane.showConfirmDialog(null,
						"Do you want to reset the terrain?", "Reset", i,
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {

					for (Player t : playerNumber) {
						t.reset();
					}
					structures = new Structures(gameType);

				}
				if (nextAnswer == JOptionPane.YES_OPTION) {
					terrain.reset();
				}
			} else if (fullReset == JOptionPane.YES_OPTION) {
				resetEverything();
			}
		}

		// Pause button
		if (e.getKeyCode() == KeyEvent.VK_P) {
			if (pause) {
				pause = false;
			} else {
				pause = true;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		// Deleting terrain
		terrain.mouseClick(e);
		requestFocusInWindow();
		hasNotClicked = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		terrain.mousePress(e);
		// Initializing the white box
		if (firstClick) {
			mouseX = e.getX();
			mouseY = e.getY();
			firstClick = false;
		}
	}

	public void mouseReleased(MouseEvent e) {
		terrain.mouseRel(e, pause);
		// Deleting the white box
		firstClick = true;
		mouseX = mouseY = boxX = boxY = mouseWidth = mouseHeight = 0;
	}

	
	// Actions performed!
	public void actionPerformed(ActionEvent e) {

		structures.actionPer(allTroops);

		// Adding the troops and refreshing them
		for (Player t : playerNumber) {
			if (allTroops.containsAll(t.totalTroops)) {
			} else {
				allTroops.removeAll(t.totalTroops);
				allTroops.addAll(t.totalTroops);
			}
			if (didNotStart) {
			}
		}

		didNotStart = false;

		// Activating the AI
		for (AI ec : AINumber) {

			ec.actionAI(e, playerNumber, terrain.totalObstructions, structures);

		}

		// Activating the Risk AI
		for (AI48 ai : AI48Number) {

			ai.actionAI48(e, playerNumber, terrain.totalObstructions, structures);

		}

		// Deleting dead troops
		if (allTroops.isEmpty()) {
		} else {
			try {
				for (Unit i : allTroops) {
					if (i != null) {
						if (i.health <= 0) {
							allTroops.remove(allTroops.indexOf(i));
						}
					} else {
						allTroops.remove(allTroops.indexOf(i));
					}
				}
			} catch (Exception e1) {
			}
		}

		// Terrain
		terrain.actionHit(e);

		int playersAlive = 4;

		// Controls for Builder box
		for (Player t : playerNumber) {

			// They do not always work

			// Win by kills
			if (kill) {
				if (t.totalKills >= killLimit) {
					JOptionPane.showMessageDialog(null, "Player "
							+ t.colorOfPlayer + " won!");
					resetEverything();
				}
			}

			// Loss by deaths
			if (death) {
				if ((t.totalDeaths >= deathLimit) || (t.playerHasDied)) {
					t.hasDied();
					playersAlive--;
					System.out.println("playersAlive: " + playersAlive);
				}
			}
			// Loss by deaths
			if (spawn) {
				if ((t.totalSpawned >= spawnLimit) || (t.playerHasDied)) {
					t.hasDied();
					playersAlive--;
					System.out.println("playersAlive: " + playersAlive);
				}
			}
			if (t.playerHasDied == false) {
				t.actionBuilder(e);
			}
			if ((playersAlive == 1) && (t.playerHasDied == false)) {
				JOptionPane.showMessageDialog(null, "Player " + t.colorOfPlayer
						+ " won!");
				resetEverything();
			}
		}

		// Pause button
		if (pause) {
			timeTilIncome--;

			// Controls for Units
			Collections.shuffle(playerNumber, RANDOM);
			for (Player t : playerNumber) {
				t.actionPer(e, playerNumber, terrain.totalObstructions,
						structures, timeTilIncome);

			}

			// Income
			if (timeTilIncome <= 0) {
				timeTilIncome = incomeRate;
			}
		}
		repaint();
	}

	// Drawing the White Box
	public void mouseDragged(MouseEvent e) {
		if (mouseX < e.getX()) {
			boxX = mouseX;
		} else {
			boxX = e.getX();
		}
		if (mouseY < e.getY()) {
			boxY = mouseY;
		} else {
			boxY = e.getY();
		}
		mouseWidth = Math.abs(mouseX - e.getX());
		mouseHeight = Math.abs(mouseY - e.getY());
	}

	public void mouseMoved(MouseEvent e) {
	}
}
