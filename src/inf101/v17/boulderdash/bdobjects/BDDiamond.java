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

		try {
			InputStream resourceAsStream = getClass().getResourceAsStream("../../../../sprites/diamond.png");
			this.image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0, 1.0, true);
		}catch (Exception e) {
			this.image = Color.AQUA;
		}
	}

	@Override
	public Paint getColor() {
		return image;
	}

	

}
