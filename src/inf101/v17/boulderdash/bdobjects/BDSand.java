package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.Paint;

import java.io.File;

/**
 * An implementation of sand which simply disappears when the player walks over
 * it. Nothing to do here.
 *
 * @author larsjaffke
 *
 */
public class BDSand extends AbstractBDObject {
	private Paint image;

	public BDSand(BDMap owner) {
		super(owner);
		this.image = owner.getSprite(1,0);
	}

	@Override
	public Paint getColor() {
		return this.image;
	}

	@Override
	public void step() {
		// DO NOTHING
	}
}
