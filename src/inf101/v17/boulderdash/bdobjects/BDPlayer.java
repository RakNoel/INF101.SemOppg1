package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.gui.SpriteReader;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.InputStream;
import java.util.ArrayList;

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
     * Number of steps taken. basicly just to show sprites
     */
    protected int stepsTaken = 0;

    /**
     * The direction indicated by keypresses.
     */
    protected Direction askedToGo;

    /**
     * Last direction player went
     */
    protected int lastDir = 0;

    /**
     * Number of diamonds collected so far.
     */
    protected int diamondCnt = 0;

    /**
     * The color /image that shall be painted for this objct
     */
    private ArrayList<Paint> images;

    public BDPlayer(BDMap owner) {
        super(owner);

        //Creates a spriteReader object to read sprites
        InputStream reAsStr = getClass().getResourceAsStream("../../../../sprites/player/playerSheet.png");
        SpriteReader SPR = new SpriteReader(reAsStr, 35, 35, 2);

        //Add a color just in case
        images = new ArrayList<>();
        images.add(Color.BLUE);

        ///Tries to read all sprites that this object should have and adds them to images list
        for (int y = 0; y < 2; y++)
            for (int x = 0; x < 8; x++)
                images.add(SPR.getSprite(x, y));
    }

    /**
     * Method used by GUI
     * @return returns the image or color that should be drawn of the playerobject
     */
    @Override
    public Paint getColor() {
        if (images.size() > 1)
            return this.images.get(((stepsTaken / 2) + 1) + (8 * lastDir));
        else
            return this.images.get(0);
    }

    /**
     * @return true if the player is alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Controlls the keyevents
     * @param key
     */
    public void keyPressed(KeyCode key) {
        switch (key) {
            case LEFT:
                this.askedToGo = Direction.WEST;
                break;
            case RIGHT:
                this.askedToGo = Direction.EAST;
                break;
            case UP:
                this.askedToGo = Direction.NORTH;
                break;
            case DOWN:
                this.askedToGo = Direction.SOUTH;
                break;
        }
    }

    @Override
    public void kill() {
        this.alive = false;
    }

    /**
     * @return Returns the number of diamonds collected so far.
     */
    public int numberOfDiamonds() {
        return diamondCnt;
    }

    /**
     * The logical step method and behavior of the player object.
     *
     * @author RakNoel
     */
    @Override
    public void step() {
        Position myPos = this.owner.getPosition(this);
        boolean meCanGo = true;

        //check to see if able to move
        if (this.askedToGo != null && this.owner.canGo(this, this.askedToGo)) {

            IBDObject obj = this.owner.get(myPos.moveDirection(this.askedToGo));

            //If able to move then check what to do with object
            if (obj instanceof BDRock) {
                meCanGo = ((BDRock) obj).push(this.askedToGo);
            } else if (obj instanceof BDDiamond) {
                this.diamondCnt++;
            } else if (obj instanceof BDBug
                    || obj instanceof BDSlimeDrop
                    || obj instanceof BDSlimeSpawn) {
                this.kill();
                meCanGo = false;
            } else if (obj instanceof BDDoor) {
                owner.finish();
                meCanGo = false;
            }

            //If actually able to walk then finally walk
            if (meCanGo) {
                try {
                    prepareMoveTo(this.askedToGo);
                } catch (IllegalMoveException e) {
                    System.err.println("Can't go there");
                }
            }

            //Uppdate sprite variables
            if (askedToGo == Direction.EAST)
                this.lastDir = 0;
            else if (askedToGo == Direction.WEST)
                this.lastDir = 1;

            //Now set askedToGo to null so we don't do it more than once
            this.askedToGo = null;
            super.step();
        }
        //Again update sprites variables
        this.stepsTaken = ++this.stepsTaken % 16;
    }

    @Override
    public boolean isKillable() {
        return true;
    }


}
