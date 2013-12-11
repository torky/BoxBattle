import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class GamePanel extends JPanel implements ActionListener, KeyListener,
		MouseListener, MouseMotionListener, Runnable {

	// Initialization
	public GamePanel() {
		tm.start();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setSize(Main.lengthOfFrame, Main.heightOfFrame - StatisticPanel.height);
		setBackground(Color.BLACK);
		teamNumber.add(team1);
		teamNumber.add(team2);
		teamNumber.add(team3);
		teamNumber.add(team4);
	}

	boolean didNotStart = true;

	Terrain terrain = new Terrain();
	ArrayList<Team> teamNumber = new ArrayList<Team>();
	ArrayList<Easy> easyNumber = new ArrayList<Easy>();
	ArrayList<AI48> AI48Number = new ArrayList<AI48>();
	ArrayList<Unit> allTroops = new ArrayList<Unit>();

	// Game conditions
	// Kills
	int kill = -1;
	int killLimit = 0;

	// Deaths
	int death = -1;
	int deathLimit = 0;

	// Spawned
	int spawn = -1;
	int spawnLimit = 0;

	int gameType = 48;
	int timeTilIncome = 0;
	int incomeRate = 500;

	// Picking game conditions
	{
		// Kill limit or not
		kill = JOptionPane.showConfirmDialog(null,
				"Do you want a kill number?", "Reset",
				JOptionPane.YES_NO_OPTION);
		if (kill == JOptionPane.YES_OPTION) {
			killLimit = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the kill number."));
		}

		// Death limit or not
		death = JOptionPane.showConfirmDialog(null,
				"Do you want a death limit?", "Reset",
				JOptionPane.YES_NO_OPTION);
		if (death == JOptionPane.YES_OPTION) {
			deathLimit = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the death limit."));
		}

		// Spawn limit or not
		spawn = JOptionPane.showConfirmDialog(null,
				"Do you want a spawn limit?", "Reset",
				JOptionPane.YES_NO_OPTION);

		if (spawn == JOptionPane.YES_OPTION) {
			spawnLimit = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the spawn limit."));
		}

		gameType = Integer.parseInt(JOptionPane
				.showInputDialog("Pick the game type 4 or 5 or 48."));

	}

	Structures structures = new Structures(gameType);

	String pop = JOptionPane.showInputDialog("Set the Maximum Population");
	int popLimiter = Integer.parseInt(pop);
	BufferedImage img = null;
	{
		try {
			img = ImageIO.read(new File("Field.jpg"));
		} catch (IOException e) {
			System.out.println("ge");
		}
	}

	// Images (Don't know why, but I can't seem to load an image)
	ImageIcon get = new ImageIcon();

	// there has to be at least 1 human player
	Team team1 = new Team(Color.RED, KeyEvent.VK_1, 0, popLimiter);
	Team team2;
	Team team3;
	Team team4;

	int areTeams;

	// Choosing the number of AIs
	{
		if (gameType == 48) {
			int AI48 = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the number of Risk AI."));
			if (AI48 == 1) {
				team2 = new AI48(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new Team(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Team(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);

				AI48Number.add((AI48) team2);

			} else if (AI48 == 2) {
				team2 = new AI48(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new AI48(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Team(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);

				AI48Number.add((AI48) team2);
				AI48Number.add((AI48) team3);

			} else if (AI48 == 3) {
				team2 = new AI48(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new AI48(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new AI48(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);

				AI48Number.add((AI48) team2);
				AI48Number.add((AI48) team3);
				AI48Number.add((AI48) team4);

			} else {
				team2 = new Team(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new Team(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Team(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);
			}
		} else {
			int EasyAI = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the number of Easy AI."));
			if (EasyAI == 1) {
				team2 = new Easy(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new Team(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Team(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);

				easyNumber.add((Easy) team2);

			} else if (EasyAI == 2) {
				team2 = new Easy(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new Easy(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Team(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);

				easyNumber.add((Easy) team2);
				easyNumber.add((Easy) team3);

			} else if (EasyAI == 3) {
				team2 = new Easy(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new Easy(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Easy(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);

				easyNumber.add((Easy) team2);
				easyNumber.add((Easy) team3);
				easyNumber.add((Easy) team4);

			} else {
				team2 = new Team(Color.YELLOW, KeyEvent.VK_B, 1, popLimiter);
				team3 = new Team(Color.MAGENTA, KeyEvent.VK_X, 2, popLimiter);
				team4 = new Team(Color.GREEN, KeyEvent.VK_SLASH, 3, popLimiter);
			}
		}

		areTeams = JOptionPane.showConfirmDialog(null, "Do you want teams?",
				"Reset", JOptionPane.YES_NO_OPTION);

		if (areTeams == JOptionPane.YES_OPTION) {

			int allyTeamNumber1 = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the Ally Number for team 1."));
			team1.setAllies(allyTeamNumber1);
			
			int allyTeamNumber2 = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the Ally Number for team 2."));
			team2.setAllies(allyTeamNumber2);
			
			int allyTeamNumber3 = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the Ally Number for team 3."));
			team3.setAllies(allyTeamNumber3);
			
			int allyTeamNumber4 = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the Ally Number for team 4."));
			team4.setAllies(allyTeamNumber4);
		}

	}

	// Timer
	Timer tm = new Timer(10, this);

	int mouseX = 0;
	int mouseY = 0;
	int boxX = 0;
	int boxY = 0;
	int mouseWidth = 0;
	int mouseHeight = 0;
	boolean firstClick = true;
	boolean pause = false;

	// ======================================PAINT-COMPONENT==========================================================//
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, 1200, 720, this);
		g.setColor(Color.BLUE);
		g.fillRect(0, Main.heightOfFrame - StatisticPanel.height,
				Main.lengthOfFrame, 100);
		g.setColor(Color.WHITE);
		g.drawString("Running: " + pause, 900, 610);
		g.drawRect(boxX, boxY, mouseWidth, mouseHeight);
		g.drawString("Kill Limit: " + killLimit, 900, 610 + 12);
		g.drawString("Death Limit: " + deathLimit, 900, 610 + 24);
		g.drawString("Spawn Limit: " + spawnLimit, 900, 610 + 36);
		g.drawString("Pop Limit: " + popLimiter, 900, 610 + 48);
		g.drawString("Time Til: " + timeTilIncome, 900, 610 + 60);

		structures.paint(g);
		terrain.draw(g);
		for (Team team : teamNumber) {
			team.paint(g, teamNumber);
		}
		repaint();
	}

	// Full reset, but doesn't reset cursor location (I have the code already
	// prepared, but I'm just not using it)
	public void resetEverything() {
		for (Team t : teamNumber) {
			t.fullReset();
		}
		System.out.println("resetstart");
		structures.reset();
		System.out.println("resetend");

		terrain.reset();
		int reset = JOptionPane.showConfirmDialog(null, "Reset rules?",
				"Reset", JOptionPane.YES_NO_OPTION);

		if (reset == JOptionPane.YES_OPTION) {
			// Kill limit or not
			kill = JOptionPane.showConfirmDialog(null,
					"Do you want a kill number?", "Reset",
					JOptionPane.YES_NO_OPTION);
			if (kill == JOptionPane.YES_OPTION) {
				killLimit = Integer.parseInt(JOptionPane
						.showInputDialog("Pick the kill number."));
			}

			// Death limit or not
			death = JOptionPane.showConfirmDialog(null,
					"Do you want a death limit?", "Reset",
					JOptionPane.YES_NO_OPTION);
			if (death == JOptionPane.YES_OPTION) {
				deathLimit = Integer.parseInt(JOptionPane
						.showInputDialog("Pick the death limit."));
			}

			// Spawn limit or not
			spawn = JOptionPane.showConfirmDialog(null,
					"Do you want a spawn limit?", "Reset",
					JOptionPane.YES_NO_OPTION);

			if (spawn == JOptionPane.YES_OPTION) {
				spawnLimit = Integer.parseInt(JOptionPane
						.showInputDialog("Pick the spawn limit."));
			}

			pop = JOptionPane.showInputDialog("Set the Maximum Population");
			popLimiter = Integer.parseInt(pop);
			for (Team t : teamNumber) {
				t.changePop(popLimiter);
			}

			gameType = Integer.parseInt(JOptionPane
					.showInputDialog("Pick the game type 4 or 5 or 48."));
		}
	}

	// Moving and spawning for team Builders
	public void keyPressed(KeyEvent e) {
		for (Team t : teamNumber) {
			boolean canType = true;
			if (spawn == JOptionPane.YES_OPTION) {
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

		// Moving and spawning for team Builders
		for (Team t : teamNumber) {
			boolean canType = true;
			if (spawn == JOptionPane.YES_OPTION) {
				if (t.totalSpawned > spawnLimit) {
					canType = false;
				}
			}
			if ((canType) && (t.teamHasDied == false)) {
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

					for (Team t : teamNumber) {
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
		for (Team t : teamNumber) {
			if (allTroops.containsAll(t.totalTroops)) {
			} else {
				allTroops.removeAll(t.totalTroops);
				allTroops.addAll(t.totalTroops);
			}
			if (didNotStart) {
				t.start();
			}
		}

		didNotStart = false;

		// Activating the Easy AI
		for (Easy ec : easyNumber) {

			ec.actionEasy(e, teamNumber, terrain.totalObstructions, structures);

		}

		// Activating the Risk AI
		for (AI48 ai : AI48Number) {

			ai.actionAI48(e, teamNumber, terrain.totalObstructions, structures);

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

		int teamsAlive = 4;

		// Controls for Builder box
		for (Team t : teamNumber) {

			// They do not always work

			// Win by kills
			if (kill == JOptionPane.YES_OPTION) {
				if (t.totalKills >= killLimit) {
					JOptionPane.showMessageDialog(null, "Player "
							+ t.colorOfTeam + " won!");
					resetEverything();
				}
			}

			// Loss by deaths
			if (death == JOptionPane.YES_OPTION) {
				if ((t.totalDeaths >= deathLimit) || (t.teamHasDied)) {
					t.hasDied();
					teamsAlive--;
					System.out.println("teamsAlive: " + teamsAlive);
				}
			}
			// Loss by deaths
			if (spawn == JOptionPane.YES_OPTION) {
				if ((t.totalSpawned >= spawnLimit) || (t.teamHasDied)) {
					t.hasDied();
					teamsAlive--;
					System.out.println("teamsAlive: " + teamsAlive);
				}
			}
			if (t.teamHasDied == false) {
				t.actionBuilder(e);
			}
			if ((teamsAlive == 1) && (t.teamHasDied == false)) {
				JOptionPane.showMessageDialog(null, "Player " + t.colorOfTeam
						+ " won!");
				resetEverything();
			}
		}

		// Pause button
		if (pause) {
			timeTilIncome--;

			// Controls for Units
			for (Team t : teamNumber) {
				t.actionPer(e, teamNumber, terrain.totalObstructions,
						structures, timeTilIncome);

			}

			// Income
			if (timeTilIncome <= 0) {
				timeTilIncome = incomeRate;
			}
		}
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

	// Yeah this one isn't being used.
	public void run() {
	}
}
