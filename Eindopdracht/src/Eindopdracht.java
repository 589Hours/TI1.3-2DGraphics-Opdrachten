import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Eindopdracht extends Application {
    private ResizableCanvas canvas;
    private FXGraphics2D g2d;
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
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
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
        stage.setTitle("Eindopdracht 2D graphics");
        stage.show();
        draw(g2d);
        controlAlert();
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        keyLogger = new KeyLogger(this);
        gameObjects = new ArrayList<>();

        Body floor = new Body();
        floor.setUserData("floor");
        BodyFixture floorFix = new BodyFixture(Geometry.createRectangle(100, 1));
        floor.addFixture(floorFix);
        floor.setMass(MassType.INFINITE);
        floor.getTransform().setTranslation(0,-5);
        world.addBody(floor);

        spawnRagdoll();
        spawnPistol();
        spawnGrenade();
    }

    private void controlAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("For controls press P");
        alert.setContentText("Thought it would be nice to know :)");
        alert.show();
    }

    private void spawnRagdoll() {
        gameObjects.add(new Ragdoll(world, new Vector2(0,0), 0.5));
    }
    private void spawnPistol() {
        Body weapon = new Body();
        //to identify if the body is a weapon or not
        weapon.setUserData("weapon");
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
        //to identify if the body is an explosive item or not
        grenade.setUserData("explosive");
        BodyFixture bodyFixture = new BodyFixture(Geometry.createCircle(0.2));
        bodyFixture.setRestitution(0.25);
        bodyFixture.setFriction(1);
        grenade.addFixture(bodyFixture);
        grenade.setMass(MassType.NORMAL);
        grenade.getTransform().setTranslation(-2, 0);
        world.addBody(grenade);
        gameObjects.add(new Bomb("/grenade.png", grenade, new Vector2(0,0), 0.15, 0.8));
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
        if (targetBody == null)
            return;

        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Bomb){

                Bomb bomb = (Bomb) gameObject;

                if (targetBody.equals(bomb.getBody())){
                    bomb.explode();
                }
            }
        }
    }
    public void shoot(){
        Body target = mousePicker.getTarget();
        Object userData = target.getUserData();

        //check for any userData if none return
        if (userData == null)
            return;

        //if the body isn't a gun: can't shoot so return
        if(!userData.equals("weapon"))
            return;

        //make a bullet and then apply force to "shoot" it
        Body bullet = new Body();
        BodyFixture bodyFixture = new BodyFixture(Geometry.createCircle(0.075));
        bodyFixture.setDensity(10);
        bodyFixture.setRestitution(0.0001);
        bullet.addFixture(bodyFixture);
        bullet.setBullet(true);
        bullet.setMass(MassType.NORMAL);
        bullet.setGravityScale(0.5);
        world.addBody(bullet);
        gameObjects.add(new Particle("redBullet.png", bullet, 0.8, new Vector2(0,0)));

        Vector2 spawnLocation = new Vector2(target.getTransform().getTranslationX(), target.getTransform().getTranslationY());

        double rotation = target.getTransform().getRotation();
        bullet.rotate(rotation);
        bullet.getTransform().setTranslation(spawnLocation);

        bullet.applyForce(new Force(Math.cos(rotation)*300,Math.sin(rotation)*300));

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
                } else if (bomb.isExploding()){
                    world.removeBody(bomb.getBody());
                    checkIntersection(bomb);
                }
                bomb.updateExplode(deltaTime);
            }
        }
        world.update(deltaTime);
    }
    private Body testPrintExplosion;
    private void checkIntersection(Bomb bomb) {

        Point2D localPos = new Point2D.Double(bomb.getCenterPoint().getX()/100, bomb.getCenterPoint().getY()/-100);

        Convex convex = Geometry.createCircle(bomb.getRadius()); //Geometry.createCircle(radius)
        testPrintExplosion = new Body();
        testPrintExplosion.setMass(MassType.INFINITE);
        testPrintExplosion.addFixture(new BodyFixture(convex));
        testPrintExplosion.getTransform().setTranslation(localPos.getX(), -localPos.getY());

        //uncommment for an explosionCircle that won't go away after detonation
        //world.addBody(testPrintExplosion);

        //translate de explosion area naar het midden
        Transform tx = new Transform();
        tx.translate(localPos.getX(), -localPos.getY());

        //detect results
        ArrayList<DetectResult> results = new ArrayList<>();
        boolean detect = world.detect(
                convex,
                tx,
                null,
                false,
                false,
                true,
                results);

        if (detect) {
            //door alle detected bodies heen
            for (DetectResult result : results) {
                Body resultBody = result.getBody();
                resultBody.setAsleep(false);
                resultBody.setAutoSleepingEnabled(false);

                //check if it is the floor or the bomb itself (since the body of the grenade will be there)
                if (resultBody == bomb.getBody() || resultBody.getUserData() == "floor")
                    continue;

                //De direction klopt nu.
                //de range van de grenade is natuurlijk vrij klein, maar zonder de 1.5
                //is de kracht nog te groot.
                Vector2 direction = result.getPenetration().getNormal();
                double initialForce = bomb.getForce();

                direction.multiply(initialForce * bomb.getRadius()/1.5);
                result.getBody().applyForce(direction);
            }
        }
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
        grenade.setOnAction(event -> spawnGrenade());

        Button ragdoll = new Button("Spawn ragdoll");
        ragdoll.setOnAction(event -> spawnRagdoll());

        GridPane gridPane = new GridPane();
        gridPane.add(pistol, 0, 0);
        gridPane.add(grenade, 1, 0);
        gridPane.add(ragdoll, 2, 0);

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
