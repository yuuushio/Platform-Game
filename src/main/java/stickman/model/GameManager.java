package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stickman.gameState.LevelState;
import stickman.gameState.TransitionState;
import stickman.memento.CareTaker;
import stickman.memento.GameMemento;
import stickman.scoreObserver.DisplayData;
import com.rits.cloning.Cloner;
import stickman.scoreObserver.Subject;
import stickman.level.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of GameEngine. Manages the running of the game.
 */
public class GameManager implements GameEngine {

    /*mine*/
    private long targetTime;
    private int currentLevelCounter = 0;
    private int totalScore;
    private long startTime;
    private long timeTicker;
    private int levelLives;
    private int tempTime;
    private boolean reloaded = false;

    private Subject subject; //need to have the subject in here to update states
    /*---*/

    /**
     * The current level
     */
    private Level level;

    /*CareTaker of the memento*/
    private CareTaker careTaker;
    GameMemento mementoTemp;

    /**
     * List of all level files
     */
    private List<String> levelFileNames;

    /**
     * Creates a GameManager object.
     * @param levels The config file containing the names of all the levels
     */
    public GameManager(String levels) {
        this.levelFileNames = this.readConfigFile(levels);

        this.level = LevelBuilderImpl.generateFromFile(levelFileNames.get(currentLevelCounter), this);

        startTime = System.currentTimeMillis();
        timeTicker = timeElapsedSeconds();
        this.levelLives = this.level.getLives();
        this.targetTime = this.level.getTargetScore();
        this.careTaker = new CareTaker();
        subject = new DisplayData();
    }

    @Override
    public int getNumLevels(){
        return this.levelFileNames.size();
    }

    @Override
    public int getCurrentLevelCounter(){
        return this.currentLevelCounter;
    }

    @Override
    public Level getCurrentLevel() {
        return this.level;
    }

    @Override
    public boolean jump() {
        return this.level.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.level.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.level.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.level.stopMoving();
    }

    @Override
    public void tick() {
        this.level.getMediatorScoreBoard().setTimeElapsed(timeElapsedSeconds());
        this.level.getMediatorScoreBoard().setCurrentTimeLapsed(tempTime);
        this.level.tick();

        //check for any changes in the score board
        if(this.level.getMediatorScoreBoard().getIsChanged()){
            totalScore += this.level.getMediatorScoreBoard().getChangeDifference();
            this.level.getMediatorScoreBoard().setChangeDifference(0);
            this.level.getMediatorScoreBoard().setIsChanged(false);
        }

        //dont time elapse when the hero finishes
        if(this.level.isActive() && !reloaded) {
            tempTime = timeElapsedSeconds()-getTimeTicker();
        }

        this.subject.setState(this.level.getLevelScore(), currentLevelCounter+1,  tempTime, (int)targetTime,totalScore, this.levelLives);
        reloaded = false; //so the time lapse continues - otherwise time lapse would get stuck after reload
    }

    public Subject getSubject(){
        return this.subject;
    }

    public int timeElapsedSeconds(){
        return (int)((System.currentTimeMillis() - startTime)/1000);
    }


    public int getTimeTicker(){
        return (int)timeTicker;
    }

    @Override
    public void setTimeTicker(){
        this.timeTicker += timeElapsedSeconds();
    }

    @Override
    public void shoot() {
        this.level.shoot();
    }

    @Override
    public void reset() {
        if(this.levelLives>=0) {
            this.levelLives -= 1;
        }if(this.levelLives == 0){
            this.level.gameOver();
        }else {
            int temp = this.level.getLevelScore(); //to keep/save current score
            this.level = LevelBuilderImpl.generateFromFile(this.level.getSource(), this);
            this.level.resetLevelScoreOnReset(temp);
        }
    }

    @Override
    public void setLevel(){
        currentLevelCounter++;
        //add to score if hero finishes before target time
        this.level.getMediatorScoreBoard().setEndLevelScore((int)targetTime-this.level.getMediatorScoreBoard().getCurrentTimeLapsed());
        this.level.setScoreandCounter(); //resets level's score and counter
        this.level.getMediatorScoreBoard().setPrevScore(0);

        totalScore += this.level.getMediatorScoreBoard().getEndLevelScore();
        this.level.getMediatorScoreBoard().setEndLevelScore(0);
        this.level.getMediatorScoreBoard().setIsChanged(false);

        this.level = LevelBuilderImpl.generateFromFile(levelFileNames.get(currentLevelCounter), this);
        timeTicker = timeElapsedSeconds();
        this.levelLives = this.level.getLives();
        this.targetTime = this.level.getTargetScore();
        this.level.getMediatorScoreBoard().setCurrentTimeLapsed(0);
    }



    //Clone reference: https://stackoverflow.com/a/22546839

    @Override
    public void setState(){
        //has to take in Long instead of int because of System.currentTimeMillis
        HashMap<String, Long> temp = new HashMap<>();
        HashMap<String, Long> currentState; //stores the cloned variables
        Level savedLevel;

        temp.put("targetTime", targetTime);
        temp.put("currentLevelCounter", (long)currentLevelCounter);
        temp.put("totalScore", (long)totalScore);
        temp.put("startTime", (System.currentTimeMillis()-startTime)/1000);
        temp.put("timeTicker", timeTicker);
        temp.put("levelLives", (long)levelLives);
        temp.put("tempTime", (long)tempTime);

        Cloner deepCloner = new Cloner();
        currentState = deepCloner.deepClone(temp);
        savedLevel = deepCloner.deepClone(this.level);
        careTaker.addState(saveStateToMemento(currentState, savedLevel));
    }

    public GameMemento saveStateToMemento(HashMap<String, Long> currentState, Level lev){
        return new GameMemento(currentState, lev);
    }


    @Override
    public void quickReload(){
        if(careTaker.getMementoSize() == 0){
            System.out.println("No states");
            return;
        }
        mementoTemp = careTaker.getLastState();
        this.level = mementoTemp.getLevel();
        HashMap<String, Long> tempState = mementoTemp.getGameEngineState();

        targetTime = tempState.get("targetTime");
        currentLevelCounter = Math.toIntExact(tempState.get("currentLevelCounter"));
        totalScore = Math.toIntExact(tempState.get("totalScore"));
        long tempStartTime = tempState.get("startTime")*1000;
        timeTicker = tempState.get("timeTicker");
        levelLives = Math.toIntExact(tempState.get("levelLives"));
        tempTime = Math.toIntExact(tempState.get("tempTime"));
        startTime = System.currentTimeMillis() - tempStartTime;
        reloaded = true;
    }



    /**
     * Retrieves the list of level filenames from a config file
     * @param config The config file
     * @return The list of level names
     */
    @SuppressWarnings("unchecked")
    private List<String> readConfigFile(String config) {

        List<String> res = new ArrayList<String>();

        JSONParser parser = new JSONParser();

        try {

            Reader reader = new FileReader(config);

            JSONObject object = (JSONObject) parser.parse(reader);

            JSONArray levelFiles = (JSONArray) object.get("levelFiles");

            Iterator<String> iterator = (Iterator<String>) levelFiles.iterator();

            // Get level file names
            while (iterator.hasNext()) {
                String file = iterator.next();
                res.add("levels/" + file);
            }

        } catch (IOException e) {
            System.exit(10);
            return null;
        } catch (ParseException e) {
            return null;
        }
        System.out.println(res);
        return res;
    }

}