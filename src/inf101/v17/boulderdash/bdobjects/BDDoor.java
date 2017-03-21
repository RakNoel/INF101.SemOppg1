package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

//Created by RakNoel, 17.03.2017.
public class BDDoor extends AbstractBDObject {

    private Paint image;

    public BDDoor(BDMap owner) {
        super(owner);

        this.image = owner.getSprite(4,0);
    }

    public Paint getColor() {
        return this.image;
    }

    public void step() {
        //Hodor! HODOR! HOOODOOOR!
    }

}
