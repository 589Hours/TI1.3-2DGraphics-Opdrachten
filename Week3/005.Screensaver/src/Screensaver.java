import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;
    private ArrayList<Point> points = new ArrayList<>();

    private ArrayList<ArrayList<Point>> lines = new ArrayList<>();
    private int linesIndex = 1;
    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
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
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.setColor(new Color(176, 38, 255));


        for (ArrayList<Point> line : lines) {
            int x1 = (int) line.get(0).getX();
            int y1 = (int) line.get(0).getY();
            int x2 = (int) line.get(1).getX();
            int y2 = (int) line.get(1).getY();
            int x3 = (int) line.get(2).getX();
            int y3 = (int) line.get(2).getY();
            int x4 = (int) line.get(3).getX();
            int y4 = (int) line.get(3).getY();

            //punten zijn een beetje raar omdat ik per ongeluk een zandloper vorm de punten heb gemaakt
            //p1 to p2
            graphics.drawLine(x1, y1, x2, y2);
            //p2 to p4
            graphics.drawLine(x2, y2, x4, y4);
            //p3 to p4
            graphics.drawLine(x4, y4, x3, y3);
            //p3 to p1
            graphics.drawLine(x3, y3, x1, y1);
        }

    }

    public void init()
    {
        addPoints();
    }

    public void update(double deltaTime)
    {
            ArrayList<Point> temporaryPoints = new ArrayList<>();
            int pointNumber = 0;
            for (Point point : points) {
                point.addOldToPointsArrayList(temporaryPoints, pointNumber);
                point.move(canvas, pointNumber);
                pointNumber++;
            }
            addToLinesArrayList(temporaryPoints);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void addToLinesArrayList(ArrayList<Point> points) {
        if (lines.size() == 50){
            if (linesIndex>=50){
                linesIndex = 1;
            }
            lines.remove(linesIndex);
        }
        lines.add(linesIndex, points);
        linesIndex++;
    }

    private void addPoints() {
        int x1 = 100;
        int y1 = 100;
        int x2 = 200;
        int y2 = 100;
        int x3 = 100;
        int y3 = 200;
        int x4 = 200;
        int y4 = 200;
        Point2D point1 = new Point2D.Double(x1, y1);
        Point2D point2 = new Point2D.Double(x2, y2);
        Point2D point3 = new Point2D.Double(x3, y3);
        Point2D point4 = new Point2D.Double(x4, y4);
        points.add(new Point(point1));
        points.add(new Point(point2));
        points.add(new Point(point3));
        points.add(new Point(point4));
        addDefaultLines();
    }
    public void addDefaultLines(){
        lines.add(points);
    }
    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }

}
