package inf101.v17.boulderdash.bdobjects;


import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

//Created by RakNoel, 15.03.2017.
public class BDRock extends AbstractBDFallingObject {

    private Paint image;

    public BDRock(BDMap owner) {
        super(owner);
        this.image = owner.getSprite(2, 0);
    }

    public Paint getColor() {
        return this.image;
    }

    public boolean push(Direction dir) {
        try {
            if (dir.equals(Direction.WEST)) {
                if (this.owner.canGo(this.getX() - 1, this.getY())
                        && this.owner.get(this.getX() - 1, this.getY()) instanceof BDEmpty) {
                    this.prepareMoveTo(dir);
                    owner.step();
                    return true;
                }
            } else if (dir.equals(Direction.EAST)) {
                if (this.owner.canGo(this.getX() + 1, this.getY())
                        && this.owner.get(this.getX() + 1, this.getY()) instanceof BDEmpty) {
                    this.prepareMoveTo(dir);
                    owner.step();
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
