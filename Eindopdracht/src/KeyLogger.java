import javafx.scene.input.KeyEvent;

public class KeyLogger {
    Eindopdracht eindopdracht;
    public KeyLogger(Eindopdracht eindopdracht){
        this.eindopdracht = eindopdracht;
    }

    public void keyPressed(KeyEvent event){
        String charFromEvent = event.getCharacter();
        switch (charFromEvent){
            case "d":
                eindopdracht.debugToggle();
                break;
            case " ":
                eindopdracht.pause();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
