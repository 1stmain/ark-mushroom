import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Window extends Application implements KinectCallbacks
{
    private GraphicsContext graphicsContext;
    private Kinect kinect;
    private int rightHandFrameCount;
    private float oldHandX;
    private float oldHandY;
    private int oldFootballX;
    private int oldFootballY;
    private Sprite handSprite;
    private Sprite footballSprite;
    private int fiftyFramesCount = 0;

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Group root = new Group();

        Scene scene = new Scene(root);

        Canvas canvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();

        root.getChildren().addAll(getBackgroundImageView(), canvas);

        primaryStage.setScene(scene);
        primaryStage.setTitle(Constants.STAGE_TITLE);
        primaryStage.show();

        initialiseSprites();

        kinect = new Kinect(this);
        kinect.start(J4KSDK.SKELETON);
    }

    private void initialiseSprites()
    {
        handSprite = new Sprite();
        handSprite.setImage("sun.png");
        handSprite.setPosition(300, 100);
        handSprite.render(graphicsContext);
        oldHandX = 300;
        oldHandY = 100;


        footballSprite = new Sprite();
        footballSprite.setImage("earth.png");
        footballSprite.setPosition(500, 500);
        footballSprite.render(graphicsContext);
        oldFootballX = 500;
        oldFootballY = 500;
    }

    private ImageView getBackgroundImageView()
    {
        try
        {
            Image backgroundImage = new Image(new FileInputStream("wood_background.jpg"));
            ImageView backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.setX(0);
            backgroundImageView.setY(0);
            backgroundImageView.setFitHeight(Constants.STAGE_HEIGHT);
            backgroundImageView.setFitWidth(Constants.STAGE_WIDTH);

            return backgroundImageView;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    @Override
    public void onRightHandMoved(float x, float y)
    {
        rightHandFrameCount++;
        fiftyFramesCount++;

        graphicsContext.clearRect(oldHandX, oldHandY, 128, 128);

        handSprite.setPosition(x, y);
        handSprite.render(graphicsContext);

        oldHandX = x;
        oldHandY = y;

        if (rightHandFrameCount == 1000 || handSprite.intersects(footballSprite))
        {
            kinect.stop();
            System.exit(0);
        }

        if (fiftyFramesCount > 9)
        {
            graphicsContext.clearRect(oldFootballX, oldFootballY, 48, 48);
            Random random = new Random();
            oldFootballX = random.nextInt(800);
            oldFootballY = random.nextInt(600);
            footballSprite.setPosition(oldFootballX, oldFootballY);
            footballSprite.render(graphicsContext);
            fiftyFramesCount = 0;
        }
    }
}