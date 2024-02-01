import java.awt.*;
import java.awt.geom.*;
    import javafx.application.Application;
    import static javafx.application.Application.launch;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Group;
    import javafx.scene.Scene;
    import javafx.scene.canvas.Canvas;
    import javafx.scene.control.ChoiceBox;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;
    import org.jfree.fx.FXGraphics2D;

    public class Spirograph extends Application {
        private TextField v1;
        private TextField v2;
        private TextField v3;
        private TextField v4;

        @Override
        public void start(Stage primaryStage) throws Exception {
            Canvas canvas = new Canvas(1920, 1080);

            VBox mainBox = new VBox();
            HBox topBar = new HBox();
            mainBox.getChildren().add(topBar);
            mainBox.getChildren().add(new Group(canvas));


//            topBar.getChildren().add();
            topBar.getChildren().add(v1 = new TextField("300")); //a
            topBar.getChildren().add(v2 = new TextField("1")); //b
            topBar.getChildren().add(v3 = new TextField("300")); //c
            topBar.getChildren().add(v4 = new TextField("10")); //d

            v1.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
            v2.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
            v3.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
            v4.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));

            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            primaryStage.setScene(new Scene(mainBox));
            primaryStage.setTitle("Spirograph");
            primaryStage.show();
        }

        public void draw(FXGraphics2D graphics) {
            //Set the color for the spiroGraph
            graphics.setColor(Color.BLACK);


            graphics.translate(1920/2, 1080/2);
            graphics.scale(1, -1);

            double a = Double.parseDouble(v1.getText());
            double b = Double.parseDouble(v2.getText());
            double c = Double.parseDouble(v3.getText());
            double d = Double.parseDouble(v4.getText());

            double n = 1;
            double resolution = 0.01;
            float step = 10000.0f;
            double lastY = a * Math.sin(b * 0) + c * Math.sin(d * 0);

            //first symbol
            for (double i = 0; i < step; i+=resolution) {
                double phi = Math.toRadians(n * i);
                double x = a * Math.cos(b * phi) + c * Math.cos(d * phi);
                double y = a * Math.sin(b * phi) + c * Math.sin(d * phi);
                graphics.draw(new Line2D.Double(x, y, (x-resolution),lastY));
                lastY = y;
            }
            //you can use Double.parseDouble(v1.getText()) to get a double value from the first textfield
            //feel free to add more textfields or other controls if needed, but beware that swing components might clash in naming
        }



        public static void main(String[] args) {
            launch(Spirograph.class);
        }

    }
