package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

//Created by RakNoel, 22.03.2017.

/**
 * Object class to handle the slipedrop that falls from the slimedpawner.
 *
 * @author RakNoel
 * @version 1.0
 * @since 22.03.2017
 */
public class BDSlimeDrop extends AbstractBDFallingObject implements IBDKillable {

    private Paint image;

    public BDSlimeDrop(BDMap owner) {
        super(owner);
        this.image = owner.getSprite(5, 0);
    }

    @Override
    public Paint getColor() {
        return this.image;
    }

    /**
     * Needs to Override due to the slimedrop de-spawning when i hits ground
     */
    @Override
    public void step() {
        Position myPos = this.owner.getPosition(this);

        try {
            IBDObject under = owner.get(myPos.getX(), myPos.getY() - 1);
            //Since fall is void we have to see for ourself if it can fall
            try {
                if (under instanceof IBDKillable) {
                    prepareMoveTo(Direction.SOUTH);
                } else if (!(under instanceof BDEmpty)) {
                    this.kill();
                } else {
                    fall();
                }
                super.step();
            } catch (Exception e) {
                System.err.println("SlimeDrop can't move correctly");
            }
        } catch (IndexOutOfBoundsException e) {
            this.kill();
        }
    }

    @Override
    public void kill() {
        owner.set(this.getX(), this.getY(), new BDEmpty(owner));
    }
}
