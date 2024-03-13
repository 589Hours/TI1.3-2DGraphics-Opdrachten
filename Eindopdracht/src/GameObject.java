import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.AffineTransform;

public interface GameObject {
    void draw(FXGraphics2D g2d);
    boolean checkIfDelete(Vector2 clickPosition);
    void deleteFromWorld();
}
