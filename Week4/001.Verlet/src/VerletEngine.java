
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class VerletEngine extends Application {

    private ResizableCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);
    private String filePath = "E:\\gitCodes\\TI1.3-2DGraphics-Opdrachten\\Week4\\001.Verlet\\saves\\SavedVerletEngine.ser";

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();

        //make button bar for save and load
        HBox top = new HBox();
        top.setSpacing(50);
        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        top.getChildren().addAll(saveButton, loadButton);
        mainPane.setTop(top);

        //save and load functionality
        saveButton.setOnAction(this::save);
        loadButton.setOnAction(this::load);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        // Mouse Events
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Verlet Engine");
        stage.show();
        draw(g2d);
    }

    public void init() {
        for (int i = 0; i < 20; i++) {
            particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        }

        for (int i = 0; i < 10; i++) {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));
        }

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint c : constraints) {
            c.draw(graphics);
        }

        for (Particle p : particles) {
            p.draw(graphics);
        }
    }

    private void update(double deltaTime) {
        for (Particle p : particles) {
            p.update((int) canvas.getWidth(), (int) canvas.getHeight());
        }

        for (Constraint c : constraints) {
            c.satisfy();
        }
    }

    private void mouseClicked(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        Particle newParticle = new Particle(mousePosition);
        particles.add(newParticle);


        if (e.getButton() == MouseButton.SECONDARY) {
            ArrayList<Particle> sorted = new ArrayList<>();
            sorted.addAll(particles);

            //sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
            Collections.sort(sorted, new Comparator<Particle>() {
                @Override
                public int compare(Particle o1, Particle o2) {
                    return (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition));
                }
            });

            if (e.isControlDown()){
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2), 100));
                constraints.add(new DistanceConstraint(newParticle, nearest, 100));
//                constraints.add(new RopeConstraint(newParticle, sorted.get(2)));
//                constraints.add(new RopeConstraint(newParticle, nearest));
            } else if(e.isShiftDown()){
                constraints.add(new DistanceConstraint(sorted.get(1), sorted.get(2)));
                particles.remove(newParticle);
            } else {
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
                constraints.add(new DistanceConstraint(newParticle, nearest));
            }


        } else if (e.getButton() == MouseButton.MIDDLE) {
            // Reset
            particles.clear();
            constraints.clear();
            init();
        } else if (e.isControlDown() && e.getButton() == MouseButton.PRIMARY){
            constraints.add(new PositionConstraint(newParticle));
        } else {
            constraints.add(new DistanceConstraint(newParticle, nearest));
        }

    }

    private Particle getNearest(Point2D point) {
        Particle nearest = particles.get(0);
        for (Particle p : particles) {
            if (p.getPosition().distance(point) < nearest.getPosition().distance(point)) {
                nearest = p;
            }
        }
        return nearest;
    }

    private void mousePressed(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10) {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e) {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e) {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    private void save(ActionEvent event) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject();
            System.out.println("Saved!");
            objectOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void load(ActionEvent event){
        try{
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void errorNoFileFound(){
        new Alert(Alert.AlertType.ERROR, "Couldn't find file" +
                "\nOr no Object found for loading");
    }
    public static void main(String[] args) {
        launch(VerletEngine.class);
    }

}
