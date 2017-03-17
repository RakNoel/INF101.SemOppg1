package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

//Created by RakNoel, 17.03.2017.
public class BDDoor extends AbstractBDObject {

    private Paint image;

    public BDDoor(BDMap owner) {
        super(owner);

        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("../../../../sprites/door.png");
            this.image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0, 1.0, true);
        }catch (Exception e) {
            this.image = Color.YELLOW;
        }
    }

    public Paint getColor() {
        return this.image;
    }

    public void step() {
        //Hodor! HODOR! HOOODOOOR!
    }

}
