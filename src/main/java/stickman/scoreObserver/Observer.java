package stickman.scoreObserver;

import java.util.HashMap;

public interface Observer {


    void update(Subject observable);
    HashMap<String, String> getScores();
}
