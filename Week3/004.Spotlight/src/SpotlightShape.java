import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class SpotlightShape {
    private Shape shape;
    private Point2D position;
    public SpotlightShape(Shape shape, Point2D position){
        this.shape = shape;
        this.position = position;
    }
    public void draw(FXGraphics2D graphics){
        Shape shapeToDraw = getFinalShape();
        graphics.draw(shapeToDraw);
    }
    public Shape getFinalShape(){
        return getTransform().createTransformedShape(shape);
    }

    public AffineTransform getTransform(){
        AffineTransform transform = new AffineTransform();
        transform.translate(position.getX(),position.getY());
        return transform;
    }

    public void setPosition(Point2D position){
        this.position = position;
    }

    public Point2D getPosition(){
        return this.position;
    }
}
