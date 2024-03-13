import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Eindopdracht extends Application {
    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private KeyLogger keyLogger;
    private boolean debugSelected = false;
    private boolean paused = false;
    private ArrayList<GameObject> gameObjects;
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        // Add debug buttom / debug button ruins keylogger for now I will bind it to key "g"
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


            InteractionContextMenu menu = new InteractionContextMenu(this, position, gameObjects);
            menu.show(mainPane, x, y);
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
        gameObjects = new ArrayList<>();

        Body floor = new Body();
        BodyFixture floorFix = new BodyFixture(Geometry.createRectangle(100, 1));
        floor.addFixture(floorFix);
        floor.setMass(MassType.INFINITE);
        floor.getTransform().setTranslation(0,-5);
        world.addBody(floor);

        // the ragdoll
        gameObjects.add(new Ragdoll(world));

        spawnPistol();
        spawnGrenade();
//      ragdoll.spawnMyRagdoll();
    }

    private void spawnPistol() {
        Body weapon = new Body();
        BodyFixture weaponFixture = new BodyFixture(Geometry.createRectangle(0.4,0.3));
        weaponFixture.setRestitution(0.25);
        weaponFixture.setFriction(1);
        weapon.addFixture(weaponFixture);
        weapon.setMass(MassType.NORMAL);
        weapon.getTransform().setTranslation(-1,0);
        world.addBody(weapon);
        gameObjects.add(new Weapon("/pistol.png", weapon, new Vector2(0,0), 0.15));
    }

    private void spawnGrenade(){
        Body grenade = new Body();
        BodyFixture bodyFixture = new BodyFixture(Geometry.createCircle(0.2));
        bodyFixture.setRestitution(0.25);
        bodyFixture.setFriction(1);
        grenade.addFixture(bodyFixture);
        grenade.setMass(MassType.NORMAL);
        grenade.getTransform().setTranslation(-2, 0);
        world.addBody(grenade);
        gameObjects.add(new Bomb("/grenade.png", grenade, new Vector2(0,0), 0.15, 0.5));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setColor(Color.white);
        graphics.clearRect(0,0, 1920, 1080);

        graphics.setTransform(camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()));
        graphics.scale(1,-1);

        for (GameObject gameObject : gameObjects) {
            gameObject.draw(graphics);
        }
        if (debugSelected){
            DebugDraw.draw(graphics, world, 100);
        }

    }

    public void explode(){
        Body targetBody = mousePicker.getTarget();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Bomb){
                if (gameObject == null)
                    return;

                Bomb bomb = (Bomb) gameObject;
                if (targetBody.equals(bomb.getBody())){
                    bomb.explode();
                }
            }
        }
    }
    public void shoot(){
        mousePicker.shoot(world);
    }

    public void update(double deltaTime) {
        if(paused)
            return;

        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext();) {
            GameObject gameObject = iterator.next();
            if(gameObject instanceof Bomb){
                Bomb bomb = (Bomb) gameObject;
                if (bomb.isExploded()){
                    gameObjects.remove(bomb);
                    world.removeBody(bomb.getBody());
                    break;
                }
                bomb.updateExplode(deltaTime);
            }
        }
        world.update(deltaTime);
    }

    public void reset(){
        init();
    }
    public void showSpawnScreen() {
        Stage itemScreen = new Stage();
        BorderPane borderPane = new BorderPane();

        Button pistol = new Button("Spawn pistol");
        pistol.setOnAction(event -> spawnPistol());

        Button grenade = new Button("Spawn grenade");

        GridPane gridPane = new GridPane();
        gridPane.add(pistol, 0, 0);
        gridPane.add(grenade, 1, 0);
        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane);
        itemScreen.setScene(scene);

        itemScreen.setTitle("Items");
        itemScreen.setWidth(750);
        itemScreen.setHeight(500);
        itemScreen.show();
    }

    public void debugToggle() {
        debugSelected = !debugSelected;
    }

    public void pause() {
        paused = !paused;
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }
}
