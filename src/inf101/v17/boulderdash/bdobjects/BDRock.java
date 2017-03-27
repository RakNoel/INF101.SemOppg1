package inf101.v17.boulderdash.bdobjects;


import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

/**
 * Class to controll the behaviour of the rock objects
 *
 * @author RakNoel
 * @version 1.0
 * @since 15.03.2017
 */
public class BDRock extends AbstractBDFallingObject {

    private Paint image;

    public BDRock(BDMap owner) {
        super(owner);
        this.image = owner.getSprite(2, 0);
    }

    public Paint getColor() {
        return this.image;
    }

    /**
     * The logical method that allows th player to push a rock either left or right.
     * @param dir The direction you want to move the rock
     * @return logical value weather the rock was able to move or not
     */
    public boolean push(Direction dir) {
        try {
            if (dir.equals(Direction.WEST)) {
                if (this.owner.canGo(this.getX() - 1, this.getY())
                        && this.owner.get(this.getX() - 1, this.getY()) instanceof BDEmpty) {
                    this.prepareMoveTo(dir);
                    this.step();
                    return true;
                }
            } else if (dir.equals(Direction.EAST)) {
                if (this.owner.canGo(this.getX() + 1, this.getY())
                        && this.owner.get(this.getX() + 1, this.getY()) instanceof BDEmpty) {
                    this.prepareMoveTo(dir);
                    this.step();
                    return true;
                }
            } else {
                throw new IllegalMoveException("Can't push rock in that direction");
            }
        } catch (IllegalMoveException e) {
            System.err.println("Can't push rock to that position");
            return false;
        }

        return false;
    }
}
