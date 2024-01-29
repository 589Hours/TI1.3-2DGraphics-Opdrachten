import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;

public class Spiral extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        //x = n * phi * cos(phi)
        //y = n * phi * sin(phi)
        double n = 1;
        double lastX = n * 0 * Math.cos(0);
        double lastY = n * 0 * Math.sin(0);
        double scale = 0.1;
        graphics.translate(1920/2, 1080/2);
        graphics.scale(2,2);
        for (double r = 0; r < 1000; r+=0.1) {
            //phi = n * r
            double phi = n * r;
            double x = n * phi * Math.cos(phi);
            double y = n * phi * Math.sin(phi);
            graphics.draw(new Line2D.Double(x, y, lastX, lastY));
            lastX = x;
            lastY = y;

        }

    }
    
    
    
    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
