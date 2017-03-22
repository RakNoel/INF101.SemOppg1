package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

//Created by RakNoel, 22.03.2017.
public class BDSlimeDrop extends AbstractBDFallingObject {

    private Paint image;

    public BDSlimeDrop(BDMap owner) {
        super(owner);
        this.image = owner.getSprite(5, 0);
    }

    public Paint getColor() {
        return this.image;
    }

    @Override
    public void step() {
        // (Try to) fall if possible
        Position myPos = this.owner.getPosition(this);
        IBDObject under = owner.get(myPos.getX(), myPos.getY() - 1);

        //Since fall is void we have to see for ourself if it can fall
        if(under instanceof BDPlayer){
            this.owner.getPlayer().kill();
        }else if (!(under instanceof BDEmpty)) {
            owner.set(this.getX(), this.getY(), new BDEmpty(owner));
        } else {
            fall();
        }
        super.step();
    }

}
