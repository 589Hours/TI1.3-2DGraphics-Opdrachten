import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private float opacity = 0.0f;
    private Image firstImage;
    private Image secondImage;
    private ArrayList<Image> images = new ArrayList<>();
    private boolean reverse = false;

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
		    if(last == -1)
                    last = now;
		    update((now - last) / 1000000000.0);
		    last = now;
		    draw(g2d);
            }
        }.start();

        addImages();
        firstImage = images.get(0);
        secondImage = images.get(1);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());

        AffineTransform tx = new AffineTransform();

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1-opacity));
        graphics.drawImage(firstImage, tx, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        graphics.drawImage(secondImage, tx, null);
    }
    

    public void update(double deltaTime) {
        if (!reverse){
            if (opacity < 0.995f){
                opacity+=0.005f;
                return;
            }
            reverse = true;
        } else {
            if (opacity >= 0.005f){
                opacity-=0.005f;
                return;
            }
            reverse = false;
        }


    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

    private void addImages(){
        try{
            images.add(ImageIO.read(getClass().getResource("/images/landscape1.jpg")));
            images.add(ImageIO.read(getClass().getResource("/images/landscape2.jpg")));
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
