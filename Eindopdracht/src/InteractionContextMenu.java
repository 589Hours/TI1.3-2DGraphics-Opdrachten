import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;

public class InteractionContextMenu extends ContextMenu {
    ArrayList<MenuItem> menuItems = new ArrayList<>();
    Vector2 clickPosition;
    public InteractionContextMenu(Vector2 clickPosition) {
        this.clickPosition = clickPosition;
        fillContextMenu();
    }

    private void fillContextMenu() {
        fillMenuItems();
        this.getItems().addAll(menuItems);
    }
    private void fillMenuItems(){
        menuItems.add(new MenuItem("Delete"));
        menuItems.add(new MenuItem("Freeze"));
        eventHandlers();
    }

    private void eventHandlers(){
        menuItems.get(0).setOnAction(event -> deleteEvent(event));
        menuItems.get(1).setOnAction(event -> freezeEvent(event));
    }

    private void deleteEvent(ActionEvent event) {

    }


    /*
        will leave freeze for what it is now. Have to think about how I will keep track of what is frozen
        without adding a boolean for every body in a ragdoll.
    */
    private void freezeEvent(ActionEvent event) {
        Body weldPosition = new Body();
        weldPosition.setMass(MassType.INFINITE);
        BodyFixture weldFixture = new BodyFixture(Geometry.createCircle(0.5));
        weldPosition.addFixture(weldFixture);
    }

}
