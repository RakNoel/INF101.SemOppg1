package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Random;

//Created by RakNoel, 22.03.2017.

/**
 * Object class to handle the behavior of the slime objects.
 *
 * @author RakNoel
 * @version 1.0
 * @since 22.03.2017
 */
public class BDSlimeSpawn extends AbstractBDKillingObject {

    /**
     * Arraylist to hold all the sprites of this object
     */
    private ArrayList<Paint> image;

    /**
     * A counter to know which sprite to return
     */
    private int counter;

    /**
     * A counter of the waitingtime til next drop-spawn starts
     */
    private int waitingTime;

    /**
     * Random
     */
    private Random rnd;

    public BDSlimeSpawn(BDMap owner) {
        super(owner);
        this.rnd = new Random();
        this.image = new ArrayList<>();
        this.counter = 0;
        this.waitingTime = rnd.nextInt(24);

        //Read all sprites
        for (int i = 6; i < 12; i++)
            this.image.add(owner.getSprite(i));
    }

    @Override
    public void step() {
        try {
            IBDObject over = owner.get(this.getX(), this.getY() + 1);

            if (over instanceof BDEmpty)
                owner.set(this.getX(), this.getY(), new BDSlimeDrop(owner));

        } catch (IndexOutOfBoundsException e) {
            System.err.println("Can't grab over slime");
        }
        if (--this.waitingTime <= 0) {
            try {
                IBDObject under = owner.get(this.getX(), this.getY() - 1);

                if (under instanceof IBDKillable && counter < 5) {
                    ++counter;
                } else if (under instanceof IBDKillable && counter >= 5) {
                    ((IBDKillable) under).kill();
                    this.owner.set(this.getX(), this.getY() - 1, new BDSlimeDrop(owner));
                } else if (under instanceof BDEmpty) {
                    if (++counter % 6 == 0) {
                        owner.set(this.getX(), this.getY() - 1, new BDSlimeDrop(owner));
                        counter = 0;
                        this.waitingTime = 24 + rnd.nextInt(8);
                    }
                } else {
                    waitingTime = 24;
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Can't grab under slime");
                waitingTime = 100000;
            }
        }
        super.step();
    }

    public Paint getColor() {
        return this.image.get(counter);
    }

}
