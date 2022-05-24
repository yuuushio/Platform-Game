package stickman.view;

import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import stickman.scoreObserver.Observer;
import stickman.scoreObserver.ScoreObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayHelper implements TextDrawer{

    private Pane pane;
    private ArrayList<Text> textArrayList;


    public DisplayHelper(){
        textArrayList = new ArrayList<>();
    }

    @Override
    public void draw(Pane pane) {
        //add text to pane
        this.pane = pane;
        int starty = 25;
        for(int i=0;i<6;i++){
            textArrayList.add(new Text());
            textArrayList.get(i).setFont(new Font(16));
            textArrayList.get(i).setX(15);
            textArrayList.get(i).setY(starty);
            starty+=18;
        }
        this.pane.getChildren().addAll(textArrayList);
    }

    @Override
    public void update(Observer o) {
        //update the text according to the new scores observed by the observer
        ScoreObserver scoreObserver = (ScoreObserver) o;
        HashMap<String, String> tempMap = scoreObserver.getScores();
        int i = 0;
        for(Map.Entry<String, String> entry : tempMap.entrySet()){
            textArrayList.get(i).setText(entry.getKey() + " " + entry.getValue());
            i++;
        }

    }
}
