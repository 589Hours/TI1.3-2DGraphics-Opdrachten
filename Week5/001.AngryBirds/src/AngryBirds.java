
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;


import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private Body redAngryBird;
    private Vector2 slingStartVector;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });
        mainPane.setTop(showDebug);


        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);

        canvas.setOnMousePressed(event -> {
            if (event.isControlDown())
                init();


            if (!event.isShiftDown())
                return;

            slingStartVector = new Vector2(event.getX(), event.getY());
        });

        canvas.setOnMouseReleased(event -> {
            if(!event.isShiftDown())
                return;

            Vector2 slingForce = new Vector2(slingStartVector.x-event.getX(), event.getY()-slingStartVector.y);
            //set normal so he flings away
            this.redAngryBird.setMass(MassType.NORMAL);
            this.redAngryBird.applyForce(slingForce);
        });

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
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        Body ground = new Body();
        BodyFixture groundFix = new BodyFixture(Geometry.createRectangle(100,1));
        ground.addFixture(groundFix);
        ground.setMass(MassType.INFINITE);
        ground.getTransform().setTranslation(0, -4.25);
        world.addBody(ground);

        Body background = new Body();
        gameObjects.add( new GameObject("/background.jpg", background, new Vector2(0,0), 1.5));

        Body slingshot = new Body();
        slingshot.getTransform().setTranslation(-5.1,-3);
        gameObjects.add(new GameObject("/slingshot.png", slingshot, new Vector2(0,0), 0.6));

        this.redAngryBird = new Body();
        redAngryBird.setUserData("redBird");
        redAngryBird.getTransform().setTranslation(-5, -2.5);
        BodyFixture redBirdFixture = new BodyFixture(Geometry.createCircle(0.2));
        redBirdFixture.setRestitution(.25);
        redBirdFixture.setDensity(2);
        redAngryBird.addFixture(redBirdFixture);
        //set infinite so he is static in air
        redAngryBird.setMass(MassType.INFINITE);
        world.addBody(redAngryBird);

        gameObjects.add(new GameObject("/redAngryBird.png", redAngryBird, new Vector2(-40,-30), 0.12));

        //wall height
        for (int y = 0; y < 4; y++) {
            //amount of walls
            for (int x = 5; x > 0; x--) {
                //skip wall 2 and 4 for 2 gaps.
                if (x == 2 || x == 4)
                    continue;

                Body woodBlock = new Body();
                BodyFixture woodBlockFixture = new BodyFixture(Geometry.createRectangle(0.5,0.5));
                woodBlockFixture.setFriction(2);
                woodBlock.addFixture(woodBlockFixture);
                woodBlock.setMass(MassType.NORMAL);
                woodBlock.getTransform().setTranslation(0.6+(0.6*x), -0.6+(-0.6*y));
                world.addBody(woodBlock);
                gameObjects.add(new GameObject("/woodBlock.png", woodBlock, new Vector2(0,0), 0.5));
            }
        }
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {

        world.update(deltaTime);
        //if you want to launch the bird mousePicker shouldn't be used

    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
