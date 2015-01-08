# BoxBattle
Game Explanation
•	A kill is your box destroying an enemy box.
•	A death is your box’s destruction. (Health of box indicated by the green bar on top of it)
•	A player creates a box by pressing a certain key.
•	The box appears where the player’s cursor is. It will auto-target enemy boxes.
•	There are 3 types of boxes: armored, regular, ranged, and wall.
•	Each takes up a certain amount of population (pop) and costs a certain amount of money
•	You obtain money every time the timer hits 0. 
  o	Armored: +2 population, $20 (more health, armored, slow movement)
  o	Regular: +1 population, $10 (standard health, standard, fast movement)
  o	Ranged: +1 population $10 (little health, ranged, medium movement)
  o	Wall: +0 pop $10 (Prodigious health, no attack, no movement) 

Quick Game
•	There are 4 players.
•	Each player tries to get 500 kills.
•	No bases

Standard Game
•	Arranged how you want it (Click on the red/green boxes to enable/disable features).
•	Green = enabled
•	Red = disabled
•	Game (4, 5, 48) 
  o	The number = the number of bases
  o	Each base is a gray box with a light-gray border
  o	Spawn a unit to capture the base or have units attack the base to capture it
  o	Each base gives a certain amount of money per turn

Statistics Panel
•	T0 – number of regular
•	T1 – number of armored
•	T2 – number of ranged
•	Po – population of all your troops
•	Ki – number of kills
•	KV – kill value (armor = +2, ranged/regular/wall = +1)
•	KD – kill/death ratio
•	2nd KV – kill value/death ratio
•	Money – total money of the player
•	Running – is the game running
•	Kill Limit – number of kills needed to win
•	Death Limit – number of deaths until a player loses
•	Spawn Limit – maximum number of troops a player can spawn
•	Population Limit – maximum population of total units
•	Time Til Salary – the time left until you get your next paycheck of money

Obstacle
•	When game is running, units can destroy the obstacles created.
•	When game is not running, units cannot destroy the obstacles created.
•	Health of the obstacle is determined by its size. (Minimum size is 30 by 30)
•	Block Obstacle – dark gray = indestructible, white – destructible
•	Slow Obstacle – gray = indestructible, light gray = destructible
•	Destructible obstacles are damaged when the unit touches it or walks on it.
AI (computer)
•	2 types – regular and the one for game (48)
•	Regular 
  o	Spawns as many troops as it can and really fast in an instance when it gets its money
•	Game (48) AI
  o	Spawns Walls at open bases
  o	Teleports whole army to attack a player after he or she controls a certain number of bases 

Game Controls

Player 1 keys (red)
•	WASD – moves cursor
•	E – armored
•	Z – regular
•	R – ranged
•	Q – wall

Player 2 keys (yellow)
•	IJKL – moves cursor
•	N – armored
•	B – regular
•	M – ranged
•	O – wall

Player 3 keys (purple)
•	WASD – moves cursor
•	C – armored
•	X – regular
•	V – ranged
•	Y – wall

Player 4 keys (blue)
•	Arrow keys – moves cursor
•	(.) Period – armored
•	(/) Slash – regular
•	(,) Comma – ranged
•	(;) Semi-colon – wall

Miscellaneous keys
•	P – pause/start (running = true/false)
•	Spacebar – reset 
•	Left-click and Drag – create block obstacle
•	Right-click and Drag – create slow obstacle (glitch-filled)
•	Simple click on an obstacle – delete obstacle
