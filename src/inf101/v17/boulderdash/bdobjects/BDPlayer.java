package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * An implementation of the player.
 *
 * @author larsjaffke
 */
public class BDPlayer extends AbstractBDMovingObject implements IBDKillable {

    /**
     * Is the player still alive?
     */
    protected boolean alive = true;

    /**
     * The direction indicated by keypresses.
     */
    protected Direction askedToGo;

    /**
     * Number of diamonds collected so far.
     */
    protected int diamondCnt = 0;

    public BDPlayer(BDMap owner) {
        super(owner);
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }

    @Override
    public Image getSprite() {
        return null;
    }

    /**
     * @return true if the player is alive
     */
    public boolean isAlive() {
        return alive;
    }

    public void keyPressed(KeyCode key) {
        switch (key) {
            case LEFT:
                askedToGo = Direction.WEST;
                break;
            case RIGHT:
                askedToGo = Direction.EAST;
                break;
            case UP:
                askedToGo = Direction.NORTH;
                break;
            case DOWN:
                askedToGo = Direction.SOUTH;
                break;
        }
    }

    @Override
    public void kill() {
        this.alive = false;
    }

    /**
     * Returns the number of diamonds collected so far.
     *
     * @return
     */
    public int numberOfDiamonds() {
        return diamondCnt;
    }

    @Override
    public void step() {
        Position myPos = this.owner.getPosition(this);
        boolean meCanGo = true;

        if (this.askedToGo != null && this.owner.canGo(this, this.askedToGo)) {

            IBDObject obj = this.owner.get(myPos.moveDirection(this.askedToGo));
            if (obj instanceof BDRock) {
                meCanGo = ((BDRock) obj).push(this.askedToGo);
            } else if (obj instanceof BDDiamond) {
                this.diamondCnt++;
            } else if (obj instanceof BDBug){
                this.kill();
                meCanGo = false;
            }

            if (meCanGo) {
                try {
                    prepareMoveTo(this.askedToGo);
                } catch (IllegalMoveException e) {
                    System.err.println("Can't go there");
                }
            }

            this.askedToGo = null;
            super.step();
        }
    }

    @Override
    public boolean isKillable() {
        return true;
    }
}
