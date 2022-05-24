package stickman.scoreObserver;
import java.util.HashMap;

public class ScoreObserver implements Observer{

    private HashMap<String, String> textArrayList;
    public ScoreObserver(){
        textArrayList = new HashMap<>();

    }
    @Override
    public void update(Subject observable) {
        DisplayData displayData = (DisplayData) observable;
        textArrayList.put("Level:", Integer.toString(displayData.getCurrentLevel()));
        textArrayList.put("Current Level Score:", Integer.toString(displayData.getCurrentLevelScore()));
        textArrayList.put("Time Elapsed:", Integer.toString(displayData.getTimeElapsed()));
        textArrayList.put("Target Time:", Integer.toString(displayData.getTargetScore()));
        textArrayList.put("Total Score:", Integer.toString(displayData.getTotalScore()));
        textArrayList.put("Lives Remaining:", Integer.toString(displayData.getLivesRemaining()));

    }

    public HashMap<String, String> getScores(){
        return this.textArrayList;
    }


}
