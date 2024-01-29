import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class Rainbow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.translate(1920/2, 1000);
        graphics.scale(1,-1);
        float radiusBinnen = 600f;
        float radiusBuiten = 300;
        double hoek = 0;


        for (int i = 0; i < 1000; i++) {
            graphics.setColor(Color.getHSBColor((float) (i/1000.0f*Math.PI), 1, 1));
            float x1 = radiusBinnen * (float)Math.cos(hoek);
            float y1 = radiusBinnen * (float)Math.sin(hoek);
            float x2 = radiusBuiten * (float)Math.cos(hoek);
            float y2 = radiusBuiten * (float)Math.sin(hoek);
            graphics.draw(new Line2D.Double(x1,y1,x2,y2));
            hoek+=0.18;
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
