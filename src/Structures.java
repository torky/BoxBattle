import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Structures {
	ArrayList<Building> totalBuildings = new ArrayList<Building>();

	int spacing = 100;
	int beginningSpacing = 25;
	int increment = 12;
	int numberOfBuildings;

	public Structures(int totalStructures) {
		numberOfBuildings = totalStructures;
		if (totalStructures == 48) {
			int totalDistance = Main.heightOfFrame - StatisticPanel.height;
			int split = (int) numberOfBuildings / increment;
			int spacePerBuilding = (int) totalDistance / split;
			for (int i = 0; i < numberOfBuildings; i++) {
				if (i * spacing + beginningSpacing > spacing * increment * 3) {
					Building building = new Building((i - increment * 3)
							* spacing + beginningSpacing, spacePerBuilding * 3
							+ beginningSpacing * 2 + 10);
					totalBuildings.add(building);
				} else if (i * spacing + beginningSpacing > spacing * increment
						* 2) {
					Building building = new Building((i - increment * 2)
							* spacing + beginningSpacing, spacePerBuilding * 2
							+ beginningSpacing * 2 + 10);
					totalBuildings.add(building);
				} else if (i * spacing + beginningSpacing > spacing * increment) {
					Building building = new Building((i - increment) * spacing
							+ beginningSpacing, spacePerBuilding * 1
							+ beginningSpacing);
					totalBuildings.add(building);
				} else {
					Building building = new Building(i * spacing
							+ beginningSpacing, beginningSpacing);
					totalBuildings.add(building);
				}
			}
		}
		if (totalStructures == 5) {
			for (int i = 0; i < numberOfBuildings; i++) {
				if (i == 0) {
					Building building = new Building(20, 20);
					totalBuildings.add(building);
				} else if (i == 1) {
					Building building = new Building(
							Main.lengthOfFrame - 50 - 20, 20);
					totalBuildings.add(building);
				} else if (i == 2) {
					Building building = new Building(20, Main.heightOfFrame
							- StatisticPanel.height - 50 - 20);
					totalBuildings.add(building);
				} else if (i == 3) {
					Building building = new Building(
							Main.lengthOfFrame - 50 - 20, Main.heightOfFrame
									- StatisticPanel.height - 50 - 20);
					totalBuildings.add(building);
				} else if (i == 4) {
					Building building = new Building(
							Main.lengthOfFrame / 2 - 25,
							(Main.heightOfFrame - StatisticPanel.height) / 2 - 25);
					totalBuildings.add(building);
				}

			}
		}
		if (totalStructures == 4) {
			for (int i = 0; i < numberOfBuildings; i++) {
				if (i == 0) {
					Building building = new Building(20, 20);
					totalBuildings.add(building);
				} else if (i == 1) {
					Building building = new Building(
							Main.lengthOfFrame - 50 - 20, 20);
					totalBuildings.add(building);
				} else if (i == 2) {
					Building building = new Building(20, Main.heightOfFrame
							- StatisticPanel.height - 50 - 20);
					totalBuildings.add(building);
				} else if (i == 3) {
					Building building = new Building(
							Main.lengthOfFrame - 50 - 20, Main.heightOfFrame
									- StatisticPanel.height - 50 - 20);
					totalBuildings.add(building);
				} else if (i == 4) {
					Building building = new Building(
							Main.lengthOfFrame / 2 - 25,
							(Main.heightOfFrame - StatisticPanel.height) / 2 - 25);
					totalBuildings.add(building);
				}

			}

		}
	}

	public void reset() {
		for (Building b : totalBuildings) {
			b.reset();
		}
	}

	public void actionPer(ArrayList<Unit> allTheTroops) {
		for (Building b : totalBuildings) {
			b.clear();
			b.actionBuilding(allTheTroops);
			for (Unit u : allTheTroops) {
				u.buildingAura(b);
			}
		}
	}

	public void paint(Graphics g) {

		for (Building b : totalBuildings) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect((int) b.x, (int) b.y, (int) b.width, (int) b.height);

			g.setColor(Color.GRAY);
			g.drawRect((int) b.x, (int) b.y, (int) b.width, (int) b.height);
			g.drawRect((int) b.x + 1, (int) b.y + 1, (int) b.width - 2,
					(int) b.height - 2);

			g.setColor(Color.BLUE);
		}
	}

}
