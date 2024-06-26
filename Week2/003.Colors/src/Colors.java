import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Colors extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Colors");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        ArrayList<Color> colors = new ArrayList<>();

        double x = 0;
        double y = 0;
        double width = 100;
        double height = 100;
        colors.add(Color.white);
        colors.add(Color.black);
        colors.add(Color.blue);
        colors.add(Color.red);
        colors.add(Color.green);
        colors.add(Color.yellow);
        colors.add(Color.orange);
        colors.add(Color.cyan);
        colors.add(Color.darkGray);
        colors.add(Color.gray);
        colors.add(Color.lightGray);
        colors.add(Color.magenta);
        colors.add(Color.pink);

        for (int i = 0; i < 13; i++) {
            Area square = new Area(new Rectangle2D.Double(x,y,width,height));
            x+=width+1;
            graphics.setColor(colors.get(i));
            graphics.fill(square);
        }

    }


    public static void main(String[] args)
    {
        launch(Colors.class);
    }

}
