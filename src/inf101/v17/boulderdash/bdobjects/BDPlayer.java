package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.gui.spriteReader;
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

    private spriteReader SPR;

    public BDPlayer(BDMap owner) {
        super(owner);
        try {
            InputStream reAsStr = getClass().getResourceAsStream("../../../../sprites/player/playerSheet.png");
            this.SPR = new spriteReader(reAsStr, 35, 35, 2);


            images = new ArrayList<>();
            images.add(Color.BLUE);

            for (int y = 0; y < 2; y++)
                for (int x = 0; x < 8; x++)
                    images.add(SPR.getSprite(x, y));


        } catch (Exception e) {
            System.err.println("Finner ikke player pritesheet");

            images = new ArrayList<>();
            images.add(Color.BLUE);

            for (int y = 0; y < 2; y++)
                for (int x = 0; x < 8; x++)
                    images.add(Color.BLUE);

        }
    }

    @Override
    public Paint getColor() {
        return this.images.get(((stepsTaken / 2) + 1) + (8 * lastDir));
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
                this.askedToGo = Direction.WEST;
                this.lastDir = 1;
                break;
            case RIGHT:
                this.askedToGo = Direction.EAST;
                this.lastDir = 0;
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
            } else if (obj instanceof BDBug) {
                this.kill();
                meCanGo = false;
            } else if (obj instanceof BDDoor) {
                owner.finish();
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
        this.stepsTaken = ++this.stepsTaken % 16;
    }

    @Override
    public boolean isKillable() {
        return true;
    }


}
