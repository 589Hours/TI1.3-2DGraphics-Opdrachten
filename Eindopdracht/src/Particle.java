import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Particle implements GameObject{
    private BufferedImage image;
    private Body body;
    private double scale;
    private Vector2 offset;

    public Particle(String imagePath, Body body, double scale, Vector2 offset) {
        this.body = body;
        this.scale = scale;
        this.offset = offset;
        try {
            this.image = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        if (image == null) {
            return;
        }

        AffineTransform tx = new AffineTransform();
        tx.translate(body.getTransform().getTranslationX() * 100, body.getTransform().getTranslationY() * 100);
        tx.rotate(body.getTransform().getRotation());
        tx.scale(scale, -scale);
        tx.translate(offset.x, offset.y);

        tx.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        g2d.drawImage(image, tx, null);
    }

    @Override
    public boolean checkIfDelete(Vector2 clickPosition) {
        return false;
    }

    @Override
    public void deleteFromWorld() {

    }
}
