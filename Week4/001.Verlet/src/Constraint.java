import org.jfree.fx.FXGraphics2D;

import java.io.Serializable;

public interface Constraint extends Serializable {
    void satisfy();
    void draw(FXGraphics2D g2d);
}