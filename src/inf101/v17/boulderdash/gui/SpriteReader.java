package inf101.v17.boulderdash.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.*;
import javafx.scene.paint.Paint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

//Created by RakNoel, 20.03.2017.
public class SpriteReader {
    private BufferedImage bImage;
    private int spriteHeight;
    private int spriteWidth;
    private int spriteBuffer;

    public SpriteReader(InputStream inStream, int spriteHeight, int spriteWidth, int spriteBuffer){
        try {
            this.bImage = ImageIO.read(inStream);
        }catch (IOException | IllegalArgumentException e){
            System.err.println("Finner ikke sprite");
            this.bImage = null;
        }
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        this.spriteBuffer = spriteBuffer;
    }

    public Paint getSprite(int x, int y) {
        x *= (spriteWidth + spriteBuffer);
        y *= (spriteHeight + spriteBuffer);

        try {
            BufferedImage holder = bImage.getSubimage(x, y, spriteWidth, spriteHeight);

            return new ImagePattern(SwingFXUtils.toFXImage(holder, null));
        }catch (NullPointerException e){
            return javafx.scene.paint.Color.BLACK;
        }
    }
}
