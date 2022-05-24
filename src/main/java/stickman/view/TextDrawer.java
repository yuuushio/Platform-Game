package stickman.view;

import javafx.scene.layout.Pane;
import stickman.scoreObserver.Observer;

public interface TextDrawer {


    void draw(Pane pane);

    void update(Observer o);
}
