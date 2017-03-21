package inf101.v17.boulderdash.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

//Created by RakNoel, 20.03.2017.
public class spriteReader {
    private BufferedImage bImage;
    private int spriteHeight;
    private int spriteWidth;
    private int spriteBuffer;

    public spriteReader(InputStream inStream, int spriteHeight, int spriteWidth, int spriteBuffer) throws Exception {
        this.bImage = ImageIO.read(inStream);
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        this.spriteBuffer = spriteBuffer;
    }

    public Paint getSprite(int x, int y) throws Exception {
        x *= (spriteWidth + spriteBuffer);
        y *= (spriteHeight + spriteBuffer);
        BufferedImage holder = bImage.getSubimage(x, y, spriteWidth, spriteHeight);

        return new ImagePattern(SwingFXUtils.toFXImage(holder, null));
    }
}
