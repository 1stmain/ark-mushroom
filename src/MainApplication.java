import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainApplication extends Application implements KinectHelperCallback
{
    private GraphicsContext graphicsContext;
    private KinectHelper kinect;
    private Image cursorImage;
    private float oldX;
    private float oldY;

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

        VBox textureVBox = new VBox(32);
        textureVBox.setAlignment(Pos.CENTER);
        textureVBox.setPadding(new Insets(64, 32, 64, 32));
        textureVBox.getChildren().addAll(getTextureButtons());

        VBox pictureVBox = new VBox(32);
        pictureVBox.setAlignment(Pos.CENTER);
        pictureVBox.setPadding(new Insets(64, 32, 64, 32));
        pictureVBox.getChildren().addAll(getPictureButtons());

        AnchorPane anchorPane = new AnchorPane(textureVBox, pictureVBox);
        AnchorPane.setLeftAnchor(textureVBox, 0.0);
        AnchorPane.setRightAnchor(pictureVBox, 0.0);

        Canvas canvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();

        StackPane rootStackPane = new StackPane(anchorPane, canvas);

        Scene scene = new Scene(rootStackPane);

        primaryStage.setScene(scene);
        primaryStage.show();

        cursorImage = new Image(new FileInputStream("images\\hand.jpg"));

        kinect = new KinectHelper(this);
        kinect.start(J4KSDK.SKELETON);
    }

    private ArrayList<ImageView> getTextureButtons()
    {
        ArrayList<ImageView> textureImageViews = new ArrayList<>();

        try
        {
            DropShadow dropShadow = new DropShadow(16, Color.BLACK);

            Image texture1 = new Image(new FileInputStream("images\\texture1.png"));
            ImageView textureImageView1 = new ImageView(texture1);
            textureImageView1.setEffect(dropShadow);

            Image texture2 = new Image(new FileInputStream("images\\texture2.jpg"));
            ImageView textureImageView2 = new ImageView(texture2);
            textureImageView2.setEffect(dropShadow);

            Image texture3 = new Image(new FileInputStream("images\\texture3.jpg"));
            ImageView textureImageView3 = new ImageView(texture3);
            textureImageView3.setEffect(dropShadow);

            Image texture4 = new Image(new FileInputStream("images\\texture4.png"));
            ImageView textureImageView4 = new ImageView(texture4);
            textureImageView4.setEffect(dropShadow);

            Image texture5 = new Image(new FileInputStream("images\\texture5.png"));
            ImageView textureImageView5 = new ImageView(texture5);
            textureImageView5.setEffect(dropShadow);

            textureImageViews.add(textureImageView1);
            textureImageViews.add(textureImageView2);
            textureImageViews.add(textureImageView3);
            textureImageViews.add(textureImageView4);
            textureImageViews.add(textureImageView5);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return textureImageViews;
    }

    private ArrayList<ImageView> getPictureButtons()
    {
        ArrayList<ImageView> pictureImageViews = new ArrayList<>();

        try
        {
            DropShadow dropShadow = new DropShadow(16, Color.BLACK);

            Image picture1 = new Image(new FileInputStream("images\\picture1.jpeg"));
            ImageView pictureImageView1 = new ImageView(picture1);
            pictureImageView1.setEffect(dropShadow);

            Image picture2 = new Image(new FileInputStream("images\\picture2.jpg"));
            ImageView pictureImageView2 = new ImageView(picture2);
            pictureImageView2.setEffect(dropShadow);

            Image picture3 = new Image(new FileInputStream("images\\picture3.jpg"));
            ImageView pictureImageView3 = new ImageView(picture3);
            pictureImageView3.setEffect(dropShadow);

            Image picture4 = new Image(new FileInputStream("images\\picture4.jpg"));
            ImageView pictureImageView4 = new ImageView(picture4);
            pictureImageView4.setEffect(dropShadow);

            Image picture5 = new Image(new FileInputStream("images\\picture5.jpg"));
            ImageView pictureImageView5 = new ImageView(picture5);
            pictureImageView5.setEffect(dropShadow);

            pictureImageViews.add(pictureImageView1);
            pictureImageViews.add(pictureImageView2);
            pictureImageViews.add(pictureImageView3);
            pictureImageViews.add(pictureImageView4);
            pictureImageViews.add(pictureImageView5);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return pictureImageViews;
    }

    @Override
    public void onRightHandMoved(float x, float y)
    {
        graphicsContext.clearRect(oldX, oldY, 50, 50);
        graphicsContext.drawImage(cursorImage, x, y);
        /*graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(x, y, 10, 10);*/
        oldX = x;
        oldY = y;
    }
}