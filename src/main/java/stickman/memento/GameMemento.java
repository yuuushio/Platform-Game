package stickman.memento;

//Clone reference: https://stackoverflow.com/a/22546839

import com.rits.cloning.Cloner;
import stickman.level.Level;

import java.util.HashMap;

public class GameMemento {
    private Level level;
    private HashMap<String, Long> state;
    private Cloner deepCloner = new Cloner();

    public GameMemento(HashMap<String, Long> state, Level level){
        this.level = level;
        this.state = state;
    }

    public HashMap<String, Long> getGameEngineState(){
        return deepCloner.deepClone(state);
    }

    public Level getLevel(){
        return deepCloner.deepClone(level);
    }

}
