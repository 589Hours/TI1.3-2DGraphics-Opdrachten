import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1024, 768);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        int leftSide = 100;
        int rightSide = 924;
        int bottomY = 600;
        //left roof piece
        graphics.drawLine(leftSide, 200, 512, 50);
        //right roof piece
        graphics.drawLine(512, 50, rightSide, 200);
        // left wall
        graphics.drawLine(leftSide, 200, leftSide, bottomY);
        //right wall
        graphics.drawLine(rightSide, 200, rightSide, bottomY);
        //floor
        graphics.drawLine(100, bottomY, 924, bottomY);
        //door
        graphics.drawLine(150, bottomY, 150, 400);
        graphics.drawLine(150, 400, 300, 400);
        graphics.drawLine(300, 400, 300 , bottomY);
        // window
        graphics.drawLine(400, 300, 700, 300);
        graphics.drawLine(400, 500, 700, 500);

        graphics.drawLine(400, 300, 400, 500);
        graphics.drawLine(700, 300, 700, 500);
        // test
    }



    public static void main(String[] args) {
        launch(House.class);
    }

}
