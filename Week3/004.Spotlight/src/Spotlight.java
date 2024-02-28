import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private SpotlightShape spotlightShape;
    private double shapeWidth = 100;
    private double shapeHeight = 100;
    @Override
    public void start(Stage stage) throws Exception
    {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        Point2D position = new Point2D.Double(0, 0);
        Shape shape = new Ellipse2D.Double(0, 0, shapeWidth, shapeHeight);

        this.spotlightShape = new SpotlightShape(shape, position);

        canvas.setOnMouseMoved(this::mouseMoved);
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        };

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();
        draw(g2d);
    }




    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        this.spotlightShape.draw(graphics);
        graphics.setClip(this.spotlightShape.getFinalShape());

        Random r = new Random();
        for(int i = 1; i < 100; i++) {
            if (canvas.getHeight() == 0 || canvas.getWidth() == 0){
                break;
            }
            graphics.setPaint(Color.getHSBColor(r.nextFloat(),1,1));
            graphics.drawLine(r.nextInt() % (int) canvas.getWidth(), r.nextInt() % (int) canvas.getHeight(), r.nextInt() % (int) canvas.getWidth(), r.nextInt() % (int) canvas.getHeight());
        }
        graphics.setClip(null);
    }
    private void mouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        Point2D newPosition = new Point2D.Double(mouseX-50, mouseY-50);
        this.spotlightShape.setPosition(newPosition);

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }
    public void init()
    {

    }

    public void update(double deltaTime)
    {

    }

    public static void main(String[] args)
    {
        launch(Spotlight.class);
    }

}
