package inf101.v17.boulderdash.bdobjects;


import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

//Created by RakNoel, 15.03.2017.
public class BDRock extends AbstractBDFallingObject {

    public BDRock(BDMap owner) {
        super(owner);
    }

    public Paint getColor() {
        return Color.DARKGRAY;
    }

    public Image getSprite() {
        return null;
    }

    public boolean push(Direction dir) {
        try {
            if (dir.equals(Direction.WEST)) {
                if (this.owner.canGo(this.getX() - 1, this.getY())
                        && this.owner.get(this.getX() - 1, this.getY()) instanceof BDEmpty) {
                    this.prepareMoveTo(dir);
                    return true;
                }
            } else if (dir.equals(Direction.EAST)) {
                if (this.owner.canGo(this.getX() + 1, this.getY())
                        && this.owner.get(this.getX() + 1, this.getY()) instanceof BDEmpty) {
                    this.prepareMoveTo(dir);
                    return true;
                }
            }else{
                throw new IllegalMoveException("Can't push rock in that direction");
            }
        } catch (IllegalMoveException e) {
            System.err.println("Can't push rock to that position");
            return false;
        }

        return false;
    }
}
