package inf101.v17.boulderdash.bdobjects;

import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.io.InputStream;

/**
 * Implementation of a piece of a wall.
 *
 * @author larsjaffke
 */
public class BDWall extends AbstractBDObject {

    private Paint image;

    public BDWall(BDMap owner) {
        super(owner);

        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("../../../../sprites/wall.png");
            this.image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0, 1.0, true);
        } catch (Exception e) {
            this.image = Color.BLACK;
        }
    }

    @Override
    public Paint getColor() {
        return this.image;
    }


    @Override
    public void step() {
        // DO NOTHING, IT'S A WALL
    }
}
