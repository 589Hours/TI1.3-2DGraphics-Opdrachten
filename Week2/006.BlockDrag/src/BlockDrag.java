import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.lang.model.type.ArrayType;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    Renderable selectedRenderable;
    ArrayList<Renderable> renderables = new ArrayList<>();
    ArrayList<Color> colors = new ArrayList<>();
    double xOffset;
    double yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        addColors();
        double width = 100;
        double height = 100;
        double x = 0;
        int i= 0;
        for (int y = 0; y < 100; y+=10) {
            Shape square = new Rectangle2D.Double(0,0, width, height);
            Point2D position = new Point2D.Double(x,y);
            Color color = colors.get(i);
            renderables.add(new Renderable(square, position, color));
            i++;
        }
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Renderable renderable : renderables) {
                renderable.draw(graphics);
        }
    }


    public static void main(String[] args)
    {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e)
    {
        Point2D position = new Point2D.Double(e.getX(), e.getY());
        for (Renderable renderable : renderables) {
            if (renderable.getFinalShape().contains(position)){
                System.out.println("start position: " + renderable.getPosition());
                selectedRenderable = renderable;
                xOffset = e.getX() - renderable.getPosition().getX();
                yOffset = e.getY() - renderable.getPosition().getY();
            }
        }
        System.out.println("Position: " + position + " offset x, y: " + xOffset +", " + yOffset);
    }

    private void mouseReleased(MouseEvent e)
    {
        System.out.println(selectedRenderable.getPosition());
        selectedRenderable = null;
    }

    private void mouseDragged(MouseEvent e)
    {
        Point2D position = new Point2D.Double(e.getX()-xOffset, e.getY()-xOffset);
        selectedRenderable.move(position);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        System.out.println("Position: " + position + " offset x, y: " + xOffset +", " + yOffset);
        System.out.println("render position: " + selectedRenderable.getPosition());
    }

    public void addColors(){
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
    }
}
