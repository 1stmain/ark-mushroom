import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Window extends Application implements KinectCallbacks
{
    private GraphicsContext graphicsContext;
    private Kinect kinect;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Group root = new Group();

        Scene scene = new Scene(root);

        Canvas canvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();

        root.getChildren().addAll(canvas);

        primaryStage.setScene(scene);
        primaryStage.setTitle(Constants.STAGE_TITLE);
        primaryStage.show();

        kinect = new Kinect(this);
        kinect.start(J4KSDK.SKELETON);
    }

    @Override
    public void onRightHandMoved(float x, float y)
    {

    }
}