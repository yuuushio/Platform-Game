package stickman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import stickman.gameState.LevelState;
import stickman.gameState.TransitionState;
import stickman.scoreObserver.Observer;
import stickman.scoreObserver.ScoreObserver;
import stickman.entity.Entity;
import stickman.model.GameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The window the Game exists within.
 */
public class GameWindow {

    /**
     * The distance from the top/bottom the player can be before the camera follows.
     */
    private static final double VIEWPORT_MARGIN_VERTICAL = 130.0;

    /**
     * The distance from the left/right side the player can be before the camera follows.
     */
    private static final double VIEWPORT_MARGIN = 280.0;

    /**
     * The width of the screen.
     */
    private final int width;

    /**
     * The height of the screen.
     */
    private final int height;

    /**
     * The current running scene.
     */
    private Scene scene;

    /**
     * The pane of the window on which sprites are projected.
     */
    private Pane pane;

    /**
     * The GameEngine of the game.
     */
    private GameEngine model;

    /**
     * A list of all the entities' views in the Game.
     */
    private List<EntityView> entityViews;

    /**
     * The background of the scene.
     */
    private BackgroundDrawer backgroundDrawer;
    private TextDrawer drawer;

    LevelState levelState;


    /**
     * The x-offset of the camera.
     */
    private double xViewportOffset = 0.0;

    /**
     * The y-offset of the camera.
     */
    private double yViewportOffset = 0.0;

    //observes score, time lapse and lives - to output onto screen
    Observer observer;
    /*TEXTs*/

    /*----*/

    /**
     * Creates a new GameWindow object.
     * @param model The GameEngine of the game
     * @param width The width of the screen
     * @param height The height of the screen
     */
    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        this.pane = new Pane();
        this.width = width;
        this.height = height;
        this.scene = new Scene(pane, width, height);
        levelState = new TransitionState();

        //register observer
        observer = new ScoreObserver();
        model.getSubject().registerObserver(observer);

        this.entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        this.backgroundDrawer = new BlockedBackground();

        //draw text based on observer
        this.drawer = new DisplayHelper();

        backgroundDrawer.draw(model, pane);
        drawer.draw( pane);
    }

    /**
     * Returns the scene.
     * @return The current scene
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Starts the game.
     */
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Draws the game (and updates it).
     */
    private void draw() {

        //need to let the game window itself to handle the level transition bc its the one displaying the level
        if(!model.getCurrentLevel().isActive() && this.model.getCurrentLevelCounter()<this.model.getNumLevels()){
            levelState.handleTransition(this.model);
        }

        model.tick();

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        heroXPos -= xViewportOffset;

        if (heroXPos < VIEWPORT_MARGIN) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= VIEWPORT_MARGIN - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width - VIEWPORT_MARGIN) {
            xViewportOffset += heroXPos - (width - VIEWPORT_MARGIN);
        }

        double heroYPos = model.getCurrentLevel().getHeroY();
        heroYPos -= yViewportOffset;

        if (heroYPos < VIEWPORT_MARGIN_VERTICAL) {
            if (yViewportOffset >= 0) { // Don't go further up than the top of the level
                yViewportOffset -= VIEWPORT_MARGIN_VERTICAL - heroYPos;
            }
        } else if (heroYPos > height - VIEWPORT_MARGIN_VERTICAL) {
            yViewportOffset += heroYPos - (height - VIEWPORT_MARGIN_VERTICAL);
        }




        for (Entity entity: entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }

        backgroundDrawer.update(xViewportOffset, yViewportOffset, model.getCurrentLevel().getFloorHeight());

        drawer.update(observer); //update text

        entityViews.removeIf(EntityView::isMarkedForDelete);
    }
}
