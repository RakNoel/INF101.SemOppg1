package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

/**
 * A diamond object. All its logic is implemented in the abstract superclass.
 *
 * @author larsjaffke
 *
 */
public class BDDiamond extends AbstractBDFallingObject {

	private Paint image;

	public BDDiamond(BDMap owner) {
		super(owner);
		this.image = owner.getSprite(3,0);
	}

	@Override
	public Paint getColor() {
		return image;
	}

	

}
