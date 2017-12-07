import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainApplication extends Application implements KinectHelperCallback
{
    private GraphicsContext graphicsContext;
    private KinectHelper kinect;
    private ArrayList<PictureButton> pictureButtons = new ArrayList<>();
    private ArrayList<TextureButton> textureButtons = new ArrayList<>();

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle(Constants.STAGE_TITLE);

        Pane root = new Pane();

        Canvas canvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();

        root.getChildren().addAll(canvas);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        initialiseButtons();

        kinect = new KinectHelper(this);
        kinect.start(J4KSDK.SKELETON);
    }

    private void initialiseButtons()
    {
        textureButtons.clear();
        pictureButtons.clear();

        try
        {
            PictureButton pictureButton1 = new PictureButton();
            pictureButton1.setImage(new FileInputStream("images\\picture1.jpeg"));
            pictureButton1.setPosition(20, 20);
            pictureButton1.render(graphicsContext);

            PictureButton pictureButton2 = new PictureButton();
            pictureButton2.setImage(new FileInputStream("images\\picture2.jpg"));
            pictureButton2.setPosition(20, 200);
            pictureButton2.render(graphicsContext);

            PictureButton pictureButton3 = new PictureButton();
            pictureButton3.setImage(new FileInputStream("images\\picture3.jpg"));
            pictureButton3.setPosition(20, 400);
            pictureButton3.render(graphicsContext);

            PictureButton pictureButton4 = new PictureButton();
            pictureButton4.setImage(new FileInputStream("images\\picture4.jpg"));
            pictureButton4.setPosition(20, 600);
            pictureButton4.render(graphicsContext);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void onRightHandMoved(float x, float y)
    {

    }
}