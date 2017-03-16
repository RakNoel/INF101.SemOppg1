package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.maps.BDMap;

import java.io.File;

/**
 * An implementation of sand which simply disappears when the player walks over
 * it. Nothing to do here.
 *
 * @author larsjaffke
 *
 */
public class BDSand extends AbstractBDObject {

	public BDSand(BDMap owner) {
		super(owner);
	}

	@Override
	public Color getColor() {
		return Color.SANDYBROWN;
	}

	@Override
	public void step() {
		// DO NOTHING
	}
}
