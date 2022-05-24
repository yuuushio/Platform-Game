package stickman;

public class MediatorScoreBoard implements Cloneable{

    private int count;
    private int prevScore;
    private boolean isChanged = false;
    private int changeDifference;
    private int timeElapsed;
    private int prevTime;
    private boolean timeChanged;
    private int timeTicker;
    private int endLevelScore;

    public MediatorScoreBoard(){
        this.count = 0;
    }

    //bullet collision counter
    public void setCount(int v){
        this.count = v;
    }
    public int getCount(){
        return this.count;
    }

    /*track change in score*/
    public void setPrevScore(int v){
        prevScore = v;
    }

    public int getPrevScore() {
        return prevScore;
    }

    public boolean getIsChanged(){
        return this.isChanged;
    }

    public void setIsChanged(boolean v){
        this.isChanged = v;
    }

    //add the difference to the total score
    public int getChangeDifference(){
        return this.changeDifference;
    }
    public void setChangeDifference(int v){
        this.changeDifference = v;
    }

    //used by level to keep track of time elapsed
    public void setTimeElapsed(int v){
        this.timeElapsed = v;
        if(this.timeElapsed != prevTime){
            timeChanged = true;
            prevTime = timeElapsed;
        }else {
            timeChanged = false;
        }
    }

    public int getTimeElapsed(){
        return this.timeElapsed;
    }

    //use this to sync score in level manager with the seconds elapsed
    public boolean getTimeChanged(){
        return this.timeChanged;
    }

    //time elapsed per level (different from total elapsed time)
    public int getCurrentTimeLapsed(){
        return this.timeTicker;
    }

    public void setCurrentTimeLapsed(int v){
        this.timeTicker = v;
    }

    //keep track of how many points the hero finished the game by
    //relative to the target time - used to add this score to total score
    public int getEndLevelScore(){
        return this.endLevelScore;
    }

    public void setEndLevelScore(int v){
        endLevelScore = v;
    }

}
