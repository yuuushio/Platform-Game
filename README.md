# Platform Game

Implementation of a **JavaFx** Stickman platform game, in a highly objected oriented style; applying SOLID and GRASP principles using various design patterns.

### Demo

<img src="https://raw.githubusercontent.com/yuuushio/Platform-Game/main/game_demo.gif" width="500"/>

### Usage

Build the code:

```bash
gradle build
```

Run the code/game:

```bash
gradle run
```

### Style
All Java code has been written following the Google Java Style Guide.

### JSON Format
* "stickmanSize": The size of the StickMan, either "normal" or "large"
* "targetTime": Target time/score for this particular level.
* "lives": Number of lives the hero has on this level.
* "stickmanPos": A JSON object storing the starting x-coordinate of the StickMan (he starts on the floor)
* "cloudVelocity": The horizontal velocity of clouds
* "levelDimensions": A JSON object storing width, height and floorHeight of the level
* "platforms": A JSON array of x,y coordinates representing locations of platforms
* "mushrooms": A JSON array of x,y coordinates representing locations of mushrooms
* "enemies": A JSON array of enemy objects
    * Each enemy is represented with a JSON object storing x, y coordinates, the sprite path, whether the enemy starts by
      moving left and the strategy used by the enemy (either "dumb", which just goes backwards and forwards, or "follow",
      which moves towards the player's location).
    * In the current set of levels, yellow slimes are set to "follow", while blue and green are set to "dumb"
* "flag": A JSON object storing the x,y coordinates of the final flag

### Different Levels
Level files are stored in `levels/` directory. `GameManager.java` reads in the list of levels from `levels.json`, and uses the first String in the "levelFiles" array as the first level for the game. To demo loading other levels, change the order of the array so that other levels can be loaded in first.

Currently the `levels.json` file contains 3 levels; to test more levels, simply add on more file names of the levels in `levels.json` file. The hero wins when it touches the flag of the last level. Game is over when the hero loses its current level's lives.

### Implemented Features
* Level Transition
* Score, Time and Lives Remaining
* Save and Load

### Controls
* Move left: `Left Arrow`
* Move right: `Right Arrow`
* Jump: `Up Arrow` 
* Shoot: `Space`
* Save: `S`
* Quick-reload: `Q`

### Collisions
Movement is configured to use a raycasting algorithm. Raycasting is where a line (ray) is projected from one object in a direction, and determines the distance to the nearest object in its path. This ensures that regardless of speed, objects will not pass through each other instead of colliding.

### Design Patterns Used & File Names
- `LevelBuilder.java` (interface) (Implements the **Builder design pattern** used to construct a new level from numerous attributes)
- `EnemyStrategy.java` (interface) (Implements the **Strategy design pattern** to define and control the movement behaviours of the enemy entity; allows us to transfer the implementation details regarding the concrete movement algorithms, to the derived classes)
- memento (package) (Implements the **Memento design pattern** to save and load the game)
    - `CareTaker.java`
    - `GameMemento.java`
    - `GameManager.java`: acts as the Originator
- gameState (package) (Implements the **State design pattern** to accommodate level transitioning)
    - `LevelState`: used to handle state change of the level displayed on game window
    - `TransitionState` : Concrete state
    - `GameWindow.java`: acts as the Context and calls `handleTransition`
- scoreObserver (package) (Implements the **Observer design pattern** to observe the changing scores & remaining lives)
    - `Subject.java`
    - `DisplayData.java`: implements Subject
    - `Observer.java`
    - `ScoreObserver.java`: implements observer

    - `GameManager.java`: sets state of the Subject
    - `GameWindow.java`: uses the observer to get the updated state
- Other
    - `MediatorScoreBoard.java`: mediates communication between the Level and GameEngine to track scores with respect to time elapsed
    - `TextDrawer.java` (interface)
    - `DisplayHelper.java`: Implements `TextDrawer`. Used to add text to pane, and then render the updated texts (scores) according to observer.
	- `LevelBuilderImpl.java`: extended to read lives and target score for the corressponding level file.

To clone the state of the system for easier and efficient save & reload, an external API was used - referenced in `build.gradle` and 
      in GameManager.java, wherever the Clone object is used. I will also list these references here:
	 - https://search.maven.org/artifact/io.github.kostaskougios/cloning/1.10.3/bundle
	 - https://stackoverflow.com/a/22546839
	 - https://github.com/kostaskougios/cloning
