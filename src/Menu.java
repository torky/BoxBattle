import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

public class Menu extends JPanel implements MouseListener, ActionListener,
		KeyListener {

	private boolean killBoolean = false;
	private boolean deathBoolean = false;
	private boolean spawnBoolean = false;

	public boolean switchedQuickGame = false;
	public boolean switchedCustomGame = false;
	public boolean switchedTeamGame = false;

	JSlider killNumber = new JSlider();
	JSlider spawnNumber = new JSlider();
	JSlider deathNumber = new JSlider();
	JSlider populationLimit = new JSlider();

	boolean teamsBoolean = false;
	JTextField team1 = new JTextField();
	JTextField team2 = new JTextField();
	JTextField team3 = new JTextField();
	JTextField team4 = new JTextField();

	JTextField aINumber = new JTextField();
	JTextField gameTypeString = new JTextField();

	Timer tm = new Timer(10, this);

	public Menu() {
		addKeyListener(this);
		addMouseListener(this);
		add(killNumber);
		add(deathNumber);
		add(spawnNumber);
		add(populationLimit);
		add(team1);
		add(team2);
		add(team3);
		add(team4);
		add(aINumber);
		add(gameTypeString);
		killNumber.setFocusable(false);
		deathNumber.setFocusable(false);
		spawnNumber.setFocusable(false);
		populationLimit.setFocusable(false);
	}

	public void paintComponent(Graphics g) {

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, Main.lengthOfFrame, Main.heightOfFrame);

		if (teamsBoolean) {
			g.setColor(Color.GREEN);
			g.fillRect(500, 100, 100, 100);
			g.setColor(Color.BLACK);
			g.drawString("Teams", 500, 150);
		} else {
			g.setColor(Color.RED);
			g.fillRect(500, 100, 100, 100);
			g.setColor(Color.BLACK);
			g.drawString("No Teams", 500, 150);
		}

		if (killBoolean) {
			g.setColor(Color.GREEN);
			g.fillRect(150, 100, 50, 25);
		} else {
			g.setColor(Color.RED);
			g.fillRect(150, 100, 50, 25);
		}

		if (deathBoolean) {
			g.setColor(Color.GREEN);
			g.fillRect(150, 150, 50, 25);
		} else {
			g.setColor(Color.RED);
			g.fillRect(150, 150, 50, 25);
		}

		if (spawnBoolean) {
			g.setColor(Color.GREEN);
			g.fillRect(150, 200, 50, 25);
		} else {
			g.setColor(Color.RED);
			g.fillRect(150, 200, 50, 25);
		}

		killNumber.setBounds(200, 100, 100, 25);
		deathNumber.setBounds(200, 150, 100, 25);
		spawnNumber.setBounds(200, 200, 100, 25);
		populationLimit.setBounds(200, 250, 100, 25);

		g.setColor(Color.BLACK);
		g.drawString("Kill Number", 200, 100);
		g.drawString("Death Number", 200, 150);
		g.drawString("Spawn Number", 200, 200);
		g.drawString("Population Limit", 200, 250);

		team1.setBounds(600, 100, 100, 25);
		team2.setBounds(600, 150, 100, 25);
		team3.setBounds(600, 200, 100, 25);
		team4.setBounds(600, 250, 100, 25);

		g.drawString("Team 1", 600, 100);
		g.drawString("Team 2", 600, 150);
		g.drawString("Team 3", 600, 200);
		g.drawString("Team 4", 600, 250);

		aINumber.setBounds(800, 100, 100, 25);
		g.drawString("AI Number", 800, 100);
		gameTypeString.setBounds(800, 150, 100, 25);
		g.drawString("Game Type(4, 5, 48, or other)", 800, 150);

		g.drawString("Quick Game", 312, 450);
		g.drawString("Custom Game", 706, 450);

		g.setColor(Color.BLUE);
		g.drawRect(300, 400, 100, 100);
		g.drawRect(700, 400, 100, 100);

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

		if (inBox(e.getX(), e.getY(), 500, 100, 100, 100)) {
			if (teamsBoolean) {
				teamsBoolean = false;
			} else {
				teamsBoolean = true;
			}
		}

		if (inBox(e.getX(), e.getY(), 150, 100, 50, 25)) {
			if (killBoolean) {
				killBoolean = false;
			} else {
				killBoolean = true;
			}
		}

		if (inBox(e.getX(), e.getY(), 150, 150, 50, 25)) {
			if (deathBoolean) {
				deathBoolean = false;
			} else {
				deathBoolean = true;
			}
		}

		if (inBox(e.getX(), e.getY(), 150, 200, 50, 25)) {
			if (spawnBoolean) {
				spawnBoolean = false;
			} else {
				spawnBoolean = true;
			}
		}

		if (inBox(e.getX(), e.getY(), 300, 400, 100, 100)) {
			createQuickGame();
		}

		if (inBox(e.getX(), e.getY(), 700, 400, 100, 100)) {
			if (teamsBoolean) {
				createTeamGame();
			} else {
				createCustomGame();
			}
		}

		repaint();
	}

	public boolean inBox(int x1, int y1, int x, int y, int width, int height) {
		if ((y1 <= y + height && y1 >= y) && (x1 <= x + width && x1 >= x)) {
			return true;
		} else {
			return false;
		}
	}

	public int getKillNumber() {
		int n = 100;
		try {
			n = killNumber.getValue()*10;
		} catch (Exception e) {
			n = 100;
		}
		return n;
	}

	public int getDeathNumber() {
		int n = 100;
		try {
			n = deathNumber.getValue()*10;
		} catch (Exception e) {
			n = 100;
		}
		return n;
	}

	public int getSpawnNumber() {
		int n = 100;
		try {
			n = spawnNumber.getValue()*10;
		} catch (Exception e) {
			n = 100;
		}
		return n;
	}

	public int getPopulationLimit() {
		int n = 500;
		try {
			n = populationLimit.getValue()*10;
		} catch (Exception e) {
			n = 500;
		}
		if (n<10){
			n = 10;
		}
		return n;
	}

	public int getTeamNumber(int playerNumber) {
		int teamNumber = playerNumber;
		if (teamsBoolean) {
			switch (playerNumber) {
			case 1:
				try {
					teamNumber = Integer.parseInt(team1.getText());
					System.out.println("gotText1:"+team1.getText());
					break;
				} catch (Exception e) {
				}
			case 2:
				try {
					teamNumber = Integer.parseInt(team2.getText());
					System.out.println("gotText2:"+team2.getText());
					break;
				} catch (Exception e) {
				}
			case 3:
				try {
					teamNumber = Integer.parseInt(team3.getText());
					System.out.println("gotText3:"+team3.getText());
					break;
				} catch (Exception e) {
				}
			case 4:
				try {
					teamNumber = Integer.parseInt(team4.getText());
					System.out.println("gotText4:"+team4.getText());
					break;
				} catch (Exception e) {
				}

			}
		}

		return teamNumber;
	}

	public int getAINumber() {
		int n = 0;
		try {
			n = Integer.parseInt(aINumber.getText());
			if ((n >= 0) && (n <= 3)) {
			} else {
				n = 0;
			}
		} catch (Exception e) {
			n = 0;
		}

		return n;
	}

	public String getGameTypeString() {
		String s = "";
		try {
			s = gameTypeString.getText();
		} catch (Exception e) {
			s = "";
		}

		return s;
	}

	public void createQuickGame() {
		GamePanel gp = new GamePanel();
		add(gp);
		removeAll();
		switchedQuickGame = true;
		System.out.println("true");
		gp.repaint();

	}

	public GamePanel createCustomGame() {

		GamePanel gp = new GamePanel(killBoolean, spawnBoolean, deathBoolean,
				getKillNumber(), getSpawnNumber(), getDeathNumber(),
				getAINumber(), getPopulationLimit(), getGameTypeString());
		add(gp);
		removeAll();
		switchedCustomGame = true;
		return gp;
	}

	public GamePanel createTeamGame() {

		GamePanel gp = new GamePanel(killBoolean, spawnBoolean, deathBoolean,
				getKillNumber(), getSpawnNumber(), getDeathNumber(),
				getAINumber(), getPopulationLimit(), getGameTypeString(),
				teamsBoolean, getTeamNumber(1), getTeamNumber(2),
				getTeamNumber(3), getTeamNumber(4));
		add(gp);
		removeAll();
		switchedTeamGame = true;
		return gp;
	}

	public void actionPerformed(ActionEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}
	
	public void removeAll(){
		removeMouseListener(this);
		removeKeyListener(this);
		remove(killNumber);
		remove(deathNumber);
		remove(spawnNumber);
		remove(populationLimit);
		remove(team1);
		remove(team2);
		remove(team3);
		remove(team4);
		remove(aINumber);
		remove(gameTypeString);
	}

}
