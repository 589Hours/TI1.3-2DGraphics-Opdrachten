import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Area mainCircle = new Area(new Ellipse2D.Double(100, 50, 400, 400));
        Area topCircle = new Area(new Ellipse2D.Double(200, 50, 200, 200));
        Area bottomCircle = new Area(new Ellipse2D.Double(200, 250, 200, 200));
        Area fillArc = new Area(new Arc2D.Double(100, 50.5, 399, 399, 90, 180, 1));

        Area littleTopCircle = new Area(new Ellipse2D.Double(275, 125, 50, 50));
        Area littleBottomCircle = new Area(new Ellipse2D.Double(275, 325, 50, 50));

        mainCircle.subtract(topCircle);
        mainCircle.subtract(fillArc);
        mainCircle.add(bottomCircle);
        mainCircle.add(littleTopCircle);
        mainCircle.subtract(littleBottomCircle);
        graphics.fill(mainCircle);
        graphics.draw(mainCircle);
    }


    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
