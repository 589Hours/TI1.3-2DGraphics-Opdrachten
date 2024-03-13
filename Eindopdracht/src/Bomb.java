import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bomb implements GameObject {
    private Body body;
    private BufferedImage image;
    private Vector2 offset;
    private double scale;
    private boolean exploding = false;
    private boolean exploded = false;
    private double Radius = 0.1;
    private double maxRadius;

    public Bomb(String imageFile, Body body, Vector2 offset, double scale, double maxRadius) {
        this.body = body;
        this.offset = offset;
        this.scale = scale;
        this.maxRadius = maxRadius;
        try {
            image = ImageIO.read(getClass().getResource(imageFile));
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
        g2d.draw(new Ellipse2D.Double(body.getTransform().getTranslationX(), body.getTransform().getTranslationY(), Radius, Radius));
    }

    public void updateExplode(double deltaTime){
        if (Radius >= maxRadius){
            exploding = false;
            exploded = true;
        }

        if (!exploding)
            return;

        Radius += deltaTime*100;
    }

    public void explode(){
        exploding = true;
    }

    public boolean isExploded() {
        return exploded;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public boolean checkIfDelete(Vector2 clickPosition) {
        return false;
    }

    @Override
    public void deleteFromWorld() {

    }


}
