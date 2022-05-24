package stickman.scoreObserver;

public interface Subject {

    void registerObserver(Observer o);
    void unregisterObserver(Observer o);
    void notifyObserver();
    void setState(int currentLevelScore, int currentLevel, int timeElapsed, int targetScore, int totalScore, int lives);

}
