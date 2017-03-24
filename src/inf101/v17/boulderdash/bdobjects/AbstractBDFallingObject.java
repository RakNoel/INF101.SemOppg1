package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

import java.util.Random;

/**
 * Contains most of the logic associated with objects that fall such as rocks
 * and diamonds.
 *
 * @author larsjaffke
 */
public abstract class AbstractBDFallingObject extends AbstractBDKillingObject {

    /**
     * A timeout between the moment when an object can fall (e.g. the tile
     * underneath it becomes empty) and the moment it does. This is necessary to
     * make sure that the player doesn't get killed immediately when walking
     * under a rock.
     */
    protected static final int WAIT = 3;

    protected boolean falling = false;

    /**
     * A counter to keep track when the falling should be executed next, see the
     * WAIT constant.
     */
    protected int fallingTimeWaited = 0;

    public AbstractBDFallingObject(BDMap owner) {
        super(owner);
    }

    /**
     * This method implements the logic of the object falling. It checks whether
     * it can fall, depending on the object in the tile underneath it and if so,
     * tries to prepare the move.
     */
    public void fall() {
        // Wait until its time to fall
        if (falling && fallingTimeWaited < WAIT) {
            fallingTimeWaited++;
            return;
        }
        // The timeout is over, try and prepare the move
        fallingTimeWaited = 0;

        Position pos = owner.getPosition(this);
        // The object cannot fall if it is on the lowest row.
        if (pos.getY() > 0) {
            try {
                // Get the object in the tile below.
                Position below = pos.moveDirection(Direction.SOUTH);
                IBDObject under = owner.get(below);

                if (falling) {
                    // fall one step if tile below is empty or killable
                    if (under instanceof BDEmpty || under instanceof IBDKillable) {
                        prepareMoveTo(Direction.SOUTH);
                    } else {
                        falling = false;
                    }
                } else {
                    // start falling if tile below is empty
                    falling = under instanceof BDEmpty;
                    fallingTimeWaited = 1;
                }
            } catch (IllegalMoveException e) {
                // This should never happen.
                System.out.println(e);
                System.exit(1);
            } catch (IndexOutOfBoundsException e) {
                //Ignore
            }
        }
    }

    @Override
    public void step() {
        try {
            IBDObject under = owner.get(this.getX(), this.getY() - 1);

            if (under instanceof AbstractBDFallingObject
                    || under instanceof BDWall) {
                boolean fallRight = false;
                boolean fallLeft = false;
                try {
                    IBDObject chk1 = owner.get(under.getX() - 1, under.getY());
                    IBDObject chk2 = owner.get(under.getX() - 1, this.getY());
                    fallLeft = (chk1 instanceof BDEmpty && chk2 instanceof BDEmpty);
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }

                try {
                    IBDObject chk3 = owner.get(under.getX() + 1, under.getY());
                    IBDObject chk4 = owner.get(under.getX() + 1, this.getY());
                    fallRight = (chk3 instanceof BDEmpty && chk4 instanceof BDEmpty);
                } catch (IndexOutOfBoundsException e) {
                    //Ignore
                }

                if (fallLeft && fallRight) {
                    if (new Random().nextBoolean())
                        this.prepareMoveTo(Direction.EAST);
                    else
                        this.prepareMoveTo(Direction.WEST);
                } else if (fallLeft) {
                    this.prepareMoveTo(Direction.WEST);
                } else if (fallRight) {
                    this.prepareMoveTo(Direction.EAST);
                }
            }
        } catch (IllegalMoveException e) {
            System.err.println("Bad move");
        } catch (IndexOutOfBoundsException e) {
            //Ignore
        }

        // (Try to) fall if possible
        fall();
        super.step();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
