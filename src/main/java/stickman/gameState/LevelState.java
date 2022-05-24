package stickman.gameState;

import stickman.model.GameEngine;

public interface LevelState {
    void handleTransition(GameEngine gameEngine);
}
