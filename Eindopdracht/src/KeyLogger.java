import javafx.scene.input.KeyEvent;

public class KeyLogger {
    Eindopdracht eindopdracht;
    public KeyLogger(Eindopdracht eindopdracht){
        this.eindopdracht = eindopdracht;
    }

    public void keyPressed(KeyEvent event){
        String charFromEvent = event.getCharacter();
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
            //this is more for debugging since I forgot to turn the keyLogger option on for canvas
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}
