package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

/**
 * An empty tile.
 *
 * @author larsjaffke
 *
 */
public class BDEmpty extends AbstractBDObject {

	private Paint image;

	public BDEmpty(BDMap owner) {
		super(owner);

		this.image = Color.TRANSPARENT;
	}

	@Override
	public Paint getColor() {
		return this.image;
	}

	@Override
	public void step() {
		// DO NOTHING
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}
