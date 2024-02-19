import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.util.ArrayList;

public class Rainbow extends Application {
    private ResizableCanvas canvas;
    private String regenboog = "";

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();

        this.regenboog = "regenboog";
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(2,2);

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 75);
        for (int i = 0; i < regenboog.length(); i++) {
            String letter = Character.toString(regenboog.charAt(i));
            AffineTransform tx = new AffineTransform();
            Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), letter).getOutline();

            tx.rotate(Math.toRadians(-90 + ((double) 180/regenboog.length()) * i));
            tx.translate(0, -100);

            Shape finalShape = tx.createTransformedShape(shape);
            graphics.setColor(Color.black);
            graphics.draw(finalShape);
            graphics.setColor(Color.getHSBColor((float) i /regenboog.length(), 1, 1));
            graphics.fill(finalShape);
        }
    }

    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
