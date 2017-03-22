package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

//Created by RakNoel, 22.03.2017.
public class BDSlimeSpawn extends AbstractBDKillingObject {

    private ArrayList<Paint> image;
    private int counter;
    private Position myPos;

    public BDSlimeSpawn(BDMap owner) {
        super(owner);
        this.image = new ArrayList<>();
        this.counter = 1;

        for (int i = 6; i < 12; i++)
            this.image.add(owner.getSprite(i));
    }
    @Override
    public void step(){
        if(myPos == null)
            this.myPos = this.owner.getPosition(this);


        IBDObject under = owner.get(myPos.getX(), myPos.getY()-1);
        if (under instanceof BDPlayer && counter < 48){
            ++counter;
        }else if(under instanceof BDEmpty){
            if(++counter%6 == 0){
                owner.set(myPos.getX(), myPos.getY()-1, new BDSlimeDrop(owner));
                counter = 0;
            }
        }
        super.step();
    }

    public Paint getColor() {
        return this.image.get(counter);
    }

}
