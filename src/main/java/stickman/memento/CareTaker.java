package stickman.memento;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private List<GameMemento> gameMementos;

    public CareTaker(){
        gameMementos = new ArrayList<>();
    }

    public void addState(GameMemento gameMemento){
        //dont keep more than 1 state if not required bc of memory
        if(gameMementos.size()>0){
            gameMementos.set(0, gameMemento);
        }else {
            gameMementos.add(gameMemento);
        }
    }

    public GameMemento getLastState(){
        GameMemento gameMemento = null;
        if(gameMementos.size() > 0){
            gameMemento = gameMementos.get(gameMementos.size()-1);
        }

        return gameMemento;
    }

    public int getMementoSize(){
        return this.gameMementos.size();
    }
}
