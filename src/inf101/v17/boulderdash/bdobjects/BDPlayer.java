package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

/**
 * An implementation of the player.
 *
 * @author larsjaffke
 *
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
	public Image getSprite() { return null; }

	/**
	 * @return true if the player is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	public void keyPressed(KeyCode key) {
		// TODO
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
		// TODO
		super.step();
	}
	
	
	@Override
	public boolean isKillable() {
		return true;
	}
}
