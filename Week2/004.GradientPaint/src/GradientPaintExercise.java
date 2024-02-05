import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));

        canvas.setOnMouseMoved(event -> {
            position = new Point2D.Double(event.getX(), event.getY());
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
    }

    public Point2D position = new Point2D.Double(1920/2,1080/2);
    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Color[] colors = new Color[]{Color.BLUE, Color.white,Color.red};
        float[] fractions = new float[]{0.3f, 0.7f, 0.9f};
        Area rectangle = new Area(new Rectangle2D.Double(0, 0,canvas.getWidth(),canvas.getHeight()));

        RadialGradientPaint radialGradientPaint =
                new RadialGradientPaint(position, 100, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        graphics.setPaint(radialGradientPaint);
        graphics.fill(rectangle);
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
