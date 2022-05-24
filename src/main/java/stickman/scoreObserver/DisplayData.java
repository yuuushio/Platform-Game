package stickman.scoreObserver;

import java.util.ArrayList;

public class DisplayData implements Subject{

    private int currentLevelScore;
    private int currentLevel;
    private int timeElapsed;
    private int targetScore;
    private int totalScore;
    private int livesRemaining;

    ArrayList<Observer> observers;

    public DisplayData(){
        observers = new ArrayList<>();
    }


    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        for(Observer o : observers){
            o.update(this);

        }
    }

    @Override
    public void setState(int currentLevelScore, int currentLevel, int timeElapsed, int targetScore, int totalScore, int lives){
        this.currentLevelScore = currentLevelScore;
        this.currentLevel = currentLevel;
        this.timeElapsed = timeElapsed;
        this.targetScore = targetScore;
        this.totalScore = totalScore;
        this.livesRemaining = lives;
        notifyObserver();
    }

    public int getCurrentLevelScore(){
        return this.currentLevelScore;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getLivesRemaining(){
        return this.livesRemaining;
    }
}
