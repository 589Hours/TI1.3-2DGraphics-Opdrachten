import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bomb implements GameObject {
    private Body body;
    private Point2D centerPoint = new Point2D.Double(0,0);
    private BufferedImage image;
    private Vector2 offset;
    private double scale;
    private boolean exploding = false;
    private boolean exploded = false;
    private double radius = 0.01;
    private double force;
    private double maxRadius;

    public Bomb(String imageFile, Body body, Vector2 offset, double scale, double maxRadius) {
        this.body = body;
        this.offset = offset;
        this.scale = scale;
        this.maxRadius = maxRadius;
        this.force = maxRadius*100;
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
    }

    public void updateExplode(double deltaTime){
        this.centerPoint = new Point2D.Double(
                this.body.getTransform().getTranslationX()*100 - radius,
                this.body.getTransform().getTranslationY()*100 - radius);

        if (radius >= maxRadius){
            exploding = false;
            exploded = true;
        }

        if (!exploding)
            return;

        radius += deltaTime*50;
    }

    public double getRadius() {
        return radius;
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

    public boolean isExploding() {
        return exploding;
    }

    public Point2D getCenterPoint() {
        return centerPoint;
    }

    public double getForce() {
        return force;
    }

    //TODO later
    @Override
    public boolean checkIfDelete(Vector2 clickPosition) {
        return false;
    }

    @Override
    public void deleteFromWorld() {

    }


}
