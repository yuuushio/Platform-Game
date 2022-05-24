package stickman.gameState;

import stickman.model.GameEngine;

public class TransitionState implements LevelState{


    @Override
    public void handleTransition(GameEngine gameEngine) {
        gameEngine.setLevel();
    }
}
