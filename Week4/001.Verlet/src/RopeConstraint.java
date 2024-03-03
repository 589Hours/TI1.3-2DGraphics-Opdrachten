import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class RopeConstraint implements Constraint{
    private double distance;
    private Particle a;
    private Particle b;

    public RopeConstraint(Particle a, Particle b) {
        this(a, b, a.getPosition().distance(b.getPosition()));
    }

    public RopeConstraint(Particle a, Particle b, double distance) {
        this.distance = distance;
        this.a = a;
        this.b = b;
    }

    @Override
    public void satisfy() {
        double currentDistance = a.getPosition().distance(b.getPosition());
        double adjustmentDistance = (currentDistance - distance) / 2;

        Point2D BA = new Point2D.Double(b.getPosition().getX() - a.getPosition().getX(), b.getPosition().getY() - a.getPosition().getY());
        double length = BA.distance(0, 0);
        if (length > 0.0001) // We kunnen alleen corrigeren als we een richting hebben
        {
            BA = new Point2D.Double(BA.getX() / length, BA.getY() / length);
        } else {
            BA = new Point2D.Double(BA.getX(), BA.getY());
        }

        a.setPosition(new Point2D.Double(a.getPosition().getX() + BA.getX() * adjustmentDistance,
                a.getPosition().getY() + BA.getY() * adjustmentDistance));
        b.setPosition(new Point2D.Double(b.getPosition().getX() - BA.getX() * adjustmentDistance,
                b.getPosition().getY() - BA.getY() * adjustmentDistance));
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        g2d.setColor(getColor());
        g2d.draw(new Line2D.Double(a.getPosition(), b.getPosition()));
    }

    private Color getColor(){
        double positionDistance = a.getPosition().distance(b.getPosition());
        double difference = positionDistance - this.distance;

        //if negative make positive so you dont have to check the negative side too
        if (difference<0){
            difference = -difference;
        }

        /* Check for the power limits
        for good: green
        for heavy but not too heavy: Orange
        for too heavy: Red
         */

        if (difference<2){
            return Color.green;
        } else if(difference > 2 && difference < 8){
            return Color.orange;
        } else {
            return Color.red;
        }
    }
    public double getDistance() {
        return distance;
    }

    public Particle getA() {
        return a;
    }

    public Particle getB() {
        return b;
    }
}
