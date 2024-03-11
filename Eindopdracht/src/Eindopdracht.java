import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Eindopdracht extends Application {
    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private KeyLogger keyLogger;
    private boolean debugSelected = false;
    private boolean paused = false;
    private ArrayList<Ragdoll> ragdolls;
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        // Add debug buttom / debug button ruins keylogger for now I will bind it to key "d"
//        javafx.scene.control.Button showDebug = new Button("Show debug");
//        showDebug.setOnAction(e -> {
//            debugSelected = !debugSelected;
//        });
//        mainPane.setTop(showDebug);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas);
        mainPane.setOnContextMenuRequested(event -> {
            double x = event.getScreenX();
            double y = event.getScreenY();
            Vector2 position = new Vector2(x,y);


            //TODO find a way to implement the event handlers from the menu
            InteractionContextMenu menu = new InteractionContextMenu(position);
            menu.show(mainPane, x, y);

            /*
                for checking if a ragdoll is selected for delete.
                maybe make method in ragdoll itself, so I can call that at the event handler?
            */
            for (Ragdoll ragdoll : ragdolls) {
                for (Body bodyPart : ragdoll.getBodyParts()) {
                    if(bodyPart.contains(position)){
                        ragdoll.deleteFromWorld();
                    }
                }
            }

        });

        canvas.setFocusTraversable(true);
        canvas.setOnKeyTyped(event -> keyLogger.keyPressed(event));
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

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Playground people :skull:");
        stage.show();
        draw(g2d);
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        keyLogger = new KeyLogger(this);
        ragdolls = new ArrayList<>();

        Body floor = new Body();
        BodyFixture floorFix = new BodyFixture(Geometry.createRectangle(100, 1));
        floor.addFixture(floorFix);
        floor.setMass(MassType.INFINITE);
        floor.getTransform().setTranslation(0,-5);
        world.addBody(floor);

        // the ragdoll
        ragdolls.add(new Ragdoll(world));
//      ragdoll.spawnMyRagdoll();
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setColor(Color.white);
        graphics.clearRect(0,0, 1920, 1080);

        graphics.setTransform(camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()));
        graphics.scale(1,-1);

        DebugDraw.draw(graphics, world, 100);
    }

    public void update(double deltaTime) {
        if(paused)
            return;

        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }

    public void debugToggle() {
        debugSelected = !debugSelected;
    }

    public void pause() {
        paused = !paused;
    }
}
