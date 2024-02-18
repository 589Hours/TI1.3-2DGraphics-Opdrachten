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

public class Mirror extends Application {
    ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        double widthDividedByTwo = canvas.getWidth()/2;
        double heightDividedByTwo = canvas.getHeight()/2;
        graphics.translate(widthDividedByTwo, heightDividedByTwo);
        graphics.scale(1,-1);

        //X-as
        graphics.drawLine((int)-widthDividedByTwo, 0, (int)widthDividedByTwo, 0);
        //Y-as
        graphics.drawLine(0, (int)-heightDividedByTwo, 0, (int)heightDividedByTwo);

        //Lijn Y = 2.5 * X
        graphics.setColor(Color.red);
        double lastY = 2.5*-widthDividedByTwo;
        double resolution = 1;
        for (double x = -widthDividedByTwo; x <= widthDividedByTwo; x+=resolution) {
            double y = 2.5 * x;
            graphics.draw(new Line2D.Double(x, y, x-resolution, lastY));
            lastY = y;
        }

        //vierkant 100x100 met middelpunt (0,150)
        graphics.setColor(Color.green);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(-50, 100, 100,100);
        graphics.draw(rectangle);

        //Spiegelen van de vierkant
        double k = 2.5;
        double first= (2/(1+Math.pow(k, 2)))-1;
        double second= (2*k)/(1+Math.pow(k, 2));
        double third = (2*k)/(1+Math.pow(k,2));
        double fourth= ( (2*Math.pow(k,2))/(1+Math.pow(k, 2)) )-1;

        AffineTransform transform = new AffineTransform(first,second,third,fourth,0,0);
        Shape mirroredRectangle = transform.createTransformedShape(rectangle);

        graphics.setColor(Color.magenta);
        graphics.draw(mirroredRectangle);
    }


    public static void main(String[] args)
    {
        launch(Mirror.class);
    }

}
