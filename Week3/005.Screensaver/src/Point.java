import org.jfree.fx.ResizableCanvas;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Point {
    private Point2D position;
    private double xDirection = 2;
    private double yDirection = 2;

    public Point(Point2D position) {
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void move(ResizableCanvas canvas, int pointNumber){
        checkIfOutOfBounds(canvas);
        double x = getX();
        double y = getY();
        switch (pointNumber){
            case 0:
                this.position = new Point2D.Double(x-xDirection, y-yDirection);
                break;
            case 1:
                this.position = new Point2D.Double(x+xDirection, y+yDirection);
                break;
            case 2:
                this.position = new Point2D.Double(x+xDirection, y-yDirection);
                break;
            case 3:
                this.position = new Point2D.Double(x-xDirection, y+yDirection);
                break;
        }
    }
    public double getX(){
        return position.getX();
    }

    public double getY(){
        return position.getY();
    }
    public void checkIfOutOfBounds(ResizableCanvas canvas){
        if (this.position.getX() < 0 || position.getX() >= canvas.getWidth()){
            xDirection= -xDirection;
        }
        if (this.position.getY() < 0 || this.position.getY() >= canvas.getHeight()){
            yDirection= -yDirection;
        }
    }

    public void addOldToPointsArrayList(ArrayList<Point> points, int index) {
        Point old = new Point(this.position);
        points.add(index, old);
    }
}
