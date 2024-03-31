import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.geometry.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InteractionContextMenu extends ContextMenu {
    private Eindopdracht eindopdracht;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private ArrayList<GameObject> gameObjects;
    private Vector2 clickPosition;
    public InteractionContextMenu(Eindopdracht eindopdracht, Vector2 clickPosition, ArrayList<GameObject> gameObjects) {
        this.eindopdracht = eindopdracht;
        this.clickPosition = clickPosition;
        this.gameObjects = gameObjects;
        fillContextMenu();
    }

    private void fillContextMenu() {
        fillMenuItems();
        this.getItems().addAll(menuItems);
    }
    private void fillMenuItems(){
        //didn't get to fix delete, so I kind of left it behind
//        menuItems.add(new MenuItem("Delete"));
        menuItems.add(new MenuItem("Reset"));
        eventHandlers();
    }

    private void eventHandlers(){
        menuItems.get(0).setOnAction(event -> resetEvent(event));
        menuItems.get(1).setOnAction(event -> freezeEvent(event));
    }

    private void resetEvent(ActionEvent event) {
        this.eindopdracht.reset();
    }


    //TODO doesn't work yet... wont detect any ragdoll
    private void deleteEvent(ActionEvent event) {
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext();) {
            GameObject gameObject = iterator.next();
            System.out.println("searching for body");

            if(gameObject.checkIfDelete(this.clickPosition)){
                System.out.println("Found body!");
//                gameObject.deleteFromWorld();
                gameObjects.remove(gameObject);
            }
        }
        System.out.println("deleted Event done!");

    }


    /*
        With Frozen I mean being Frozen in place.
        will leave freeze for what it is now. Have to think about how I will keep track of what is frozen
        without adding a boolean for every body in a ragdoll.
        I chose to skip freeze, as there were more important things as fixing the explosion.
    */
    private void freezeEvent(ActionEvent event) {
        Body weldPosition = new Body();
        weldPosition.setMass(MassType.INFINITE);
        BodyFixture weldFixture = new BodyFixture(Geometry.createCircle(0.5));
        weldPosition.addFixture(weldFixture);
    }

}
