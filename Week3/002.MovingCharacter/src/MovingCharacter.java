import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] tiles;
    private int[] walkAnimation;
    private int[] jumpAnimation;
    private int x = 0;
    private int tileChoice = 0;
    private int animationChoice = 0;
    private int timer = 0;
    private boolean jumpAnimationActive = false;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        getSubImages();
        getWalkAnimationIntegers();
        getJumpAnimationIntegers();
        canvas.setOnMouseClicked(event -> mouseClick(event));
        canvas.setOnMouseReleased(event -> mouseRelease(event));
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Image image = tiles[tileChoice];
        graphics.drawImage(image, x, (int) (canvas.getHeight()/2), null);
    }


    public void update(double deltaTime)
    {
        timer++;
        if(timer > 10) {
            if (!jumpAnimationActive) {
                x+=2;
                tileChoice = walkAnimation[animationChoice];
                if (animationChoice == 8) {
                    animationChoice = 0;
                } else {
                    animationChoice++;
                }
            } else {
                tileChoice = jumpAnimation[animationChoice];
                if (animationChoice == 8) {
                    animationChoice = 0;
                    jumpAnimationActive = false;
                } else {
                    animationChoice++;
                }
            }
            timer = 0;
        }
    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }

    public void getSubImages() {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/images/sprite.png"));
            tiles = new BufferedImage[65];
            //knip de afbeelding op in 65 stukjes van 64x64 pixels.
            for (int i = 0; i < 65; i++)
                tiles[i] = image.getSubimage(64 * (i % 8), 64 * (i / 8), 64, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getWalkAnimationIntegers() {
        walkAnimation = new int[9];
        int j = 33;
        for (int i = 0; i < 9; i++) {
            walkAnimation[i] = j;
            j++;
        }
    }
    private void getJumpAnimationIntegers(){
        jumpAnimation = new int[9];
        int j = 40;
        for (int i = 0; i < 8; i++) {
            jumpAnimation[i] = j;
            j++;
        }
    }
    private void mouseClick(MouseEvent event) {
        jumpAnimationActive = true;
        animationChoice = 0;
        tileChoice = 64;
    }
    private void mouseRelease(MouseEvent event) {
    }


}
