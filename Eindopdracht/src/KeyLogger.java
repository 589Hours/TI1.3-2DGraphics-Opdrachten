import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeyLogger {
    Eindopdracht eindopdracht;
    public KeyLogger(Eindopdracht eindopdracht){
        this.eindopdracht = eindopdracht;
    }

    public void keyPressed(KeyEvent event){
        String charFromEvent = event.getCharacter();

        //doesn't work for some reason
        if (event.getCode() == KeyCode.ESCAPE)
            showControls();

        switch (charFromEvent){
            //toggle debug
            case "g":
                eindopdracht.debugToggle();
                break;
            //pause the game
            case " ":
                eindopdracht.pause();
                break;
            //show new stage where you can spawn items
            case "\t":
                eindopdracht.showSpawnScreen();
                break;
            //shoot the weapon you are holding
            case "f":
                eindopdracht.shoot();
                break;
            //explode the explosive you are holding
            case "h":
                eindopdracht.explode();
                break;
            case "p":
                showControls();
                break;
            case "d":
                eindopdracht.rotateRight();
                break;
            case "a":
                eindopdracht.rotateLeft();
                break;
            //this is more for debugging since I forgot to turn the keyLogger option on for canvas
            default:
                System.out.println("Unknown command");
                break;
        }
    }

    private void showControls() {
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        VBox controls = new VBox();
        controls.getChildren().add(new Label("g: Toggle debug"));
        controls.getChildren().add(new Label("Space bar: Pause/Freeze game"));
        controls.getChildren().add(new Label("tab: Shows you a item spawn screen"));
        controls.getChildren().add(new Label("f: Shoots the gun you are holding"));
        controls.getChildren().add(new Label("h: explodes the explosive you are holding"));
        controls.getChildren().add(new Label("a: while holding gun rotate it left"));
        controls.getChildren().add(new Label("d: while holding gun rotate it right"));
        controls.setAlignment(Pos.CENTER);

        pane.setCenter(controls);
        Scene scene = new Scene(pane);
        stage.setTitle("controls");
        stage.setScene(scene);
        stage.setHeight(200);
        stage.setWidth(500);
        stage.show();
    }
}
