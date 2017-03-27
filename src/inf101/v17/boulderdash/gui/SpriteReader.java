package inf101.v17.boulderdash.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class used to create sprite reader objects. Theese objects will be able to read the prites from a spritesheet
 * and return the right image
 *
 * @author RakNoel
 * @version 2.3
 * @since 20.03.2017
 */
public class SpriteReader {
    /**
     * The spritesheet
     */
    private BufferedImage bImage;

    /**
     * The height of each individual sprite in teh sheet in pixels
     */
    private int spriteHeight;

    /**
     * The width of each individual sprite in the sheet in pixels
     */
    private int spriteWidth;

    /**
     * The buffer between each sprite in pixels
     */
    private int spriteBuffer;

    public SpriteReader(InputStream inStream, int spriteHeight, int spriteWidth, int spriteBuffer) {
        try {
            this.bImage = ImageIO.read(inStream);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Finner ikke sprite");
            this.bImage = null;
        }
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        this.spriteBuffer = spriteBuffer;
    }

    /**
     * Method to read and return the sprite form the sheet with (0,0) in top left corner. Bare in mind
     * that theese coordinates are NOT in pixels but counting sprites
     *
     * @param x the x Coordinate of the picture
     * @param y the y Coordinate of the picture
     * @return returns a Paint object of the sprite
     */
    public Paint getSprite(int x, int y) {
        x *= (spriteWidth + spriteBuffer);
        y *= (spriteHeight + spriteBuffer);

        try {
            BufferedImage holder = bImage.getSubimage(x, y, spriteWidth, spriteHeight);

            return new ImagePattern(SwingFXUtils.toFXImage(holder, null));
        } catch (NullPointerException e) {
            return javafx.scene.paint.Color.BLACK;
        }
    }
}
