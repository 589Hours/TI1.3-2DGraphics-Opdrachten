import javafx.scene.input.KeyEvent;

public class KeyLogger {
    Eindopdracht eindopdracht;
    public KeyLogger(Eindopdracht eindopdracht){
        this.eindopdracht = eindopdracht;
    }

    public void keyPressed(KeyEvent event){
        String charFromEvent = event.getCharacter();
        switch (charFromEvent){
            case "g":
                System.out.println("Debug activated");
                eindopdracht.debugToggle();
                break;
            case " ":
                eindopdracht.pause();
                break;
            case "\t":
                eindopdracht.showSpawnScreen();
                break;
            case "f":
                eindopdracht.shoot();
                break;
            case "h":
                eindopdracht.explode();
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
