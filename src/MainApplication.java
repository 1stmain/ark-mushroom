import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.geometry.Bounds;
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
    private GraphicsContext cursorGraphicsContext;
    private GraphicsContext textureGraphicsContext;
    private Cursor handCursor;
    private Cursor brushCursor;
    private float oldX;
    private float oldY;
    private boolean rightHandIsPushed = false;
    private ArrayList<TextureButton> textureButtons = new ArrayList<>();
    private TextureButton currentlySelectedTextureButton = null;

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
        setTextureButtons();
        for (TextureButton textureButton : this.textureButtons)
        {
            textureVBox.getChildren().add(textureButton.getImageView());
        }

        VBox pictureVBox = new VBox(32);
        pictureVBox.setAlignment(Pos.CENTER);
        pictureVBox.setPadding(new Insets(64, 32, 64, 32));
        pictureVBox.getChildren().addAll(getPictureButtons());

        AnchorPane anchorPane = new AnchorPane(textureVBox, pictureVBox);
        AnchorPane.setLeftAnchor(textureVBox, 0.0);
        AnchorPane.setRightAnchor(pictureVBox, 0.0);

        Canvas cursorCanvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        cursorGraphicsContext = cursorCanvas.getGraphicsContext2D();

        Canvas textureCanvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        textureGraphicsContext = textureCanvas.getGraphicsContext2D();

        StackPane rootStackPane = new StackPane(anchorPane, textureCanvas, cursorCanvas);
        rootStackPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(rootStackPane);

        primaryStage.setScene(scene);
        primaryStage.show();

        KinectHelper kinect = new KinectHelper(this);
        kinect.start(J4KSDK.SKELETON);

        handCursor = new Cursor();
        handCursor.setImage(new FileInputStream("images\\hand.png"));

        brushCursor = new Cursor();
        brushCursor.setImage(new FileInputStream("images\\brush.png"));
    }

    private void setTextureButtons()
    {
        try
        {
            DropShadow dropShadow = new DropShadow(16, Color.BLACK);

            Image texture1 = new Image(new FileInputStream("images\\texture1.png"));
            ImageView textureImageView1 = new ImageView(texture1);
            textureImageView1.setEffect(dropShadow);
            TextureButton textureButton1 = new TextureButton(textureImageView1);
            textureButton1.setTextureId(1);

            Image texture2 = new Image(new FileInputStream("images\\texture2.jpg"));
            ImageView textureImageView2 = new ImageView(texture2);
            textureImageView2.setEffect(dropShadow);
            TextureButton textureButton2 = new TextureButton(textureImageView2);
            textureButton2.setTextureId(2);

            Image texture3 = new Image(new FileInputStream("images\\texture3.jpg"));
            ImageView textureImageView3 = new ImageView(texture3);
            textureImageView3.setEffect(dropShadow);
            TextureButton textureButton3 = new TextureButton(textureImageView3);
            textureButton3.setTextureId(3);

            textureButtons.add(textureButton1);
            textureButtons.add(textureButton2);
            textureButtons.add(textureButton3);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
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

            pictureImageViews.add(pictureImageView1);
            pictureImageViews.add(pictureImageView2);
            pictureImageViews.add(pictureImageView3);
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
        if (rightHandIsPushed && x >= 300 && x <= 1000)
        {
            // Draw the brush cursor
            cursorGraphicsContext.clearRect(oldX, oldY, 100, 100);
            brushCursor.setPosition(x, y);
            brushCursor.render(cursorGraphicsContext);

            if (currentlySelectedTextureButton != null)
            {
                switch (currentlySelectedTextureButton.getTextureId())
                {
                    case 1:
                        textureGraphicsContext.setFill(Color.BLUE);
                        textureGraphicsContext.fillOval(x, y, 10, 10);
                        break;
                    case 2:
                        textureGraphicsContext.setFill(Color.GREEN);
                        textureGraphicsContext.fillRect(x, y, 10, 10);
                        break;
                    case 3:
                        textureGraphicsContext.setFill(Color.RED);
                        textureGraphicsContext.fillOval(x, y, 10, 10);
                        break;
                    default:
                        break;

                }
            }
        }
        else
        {
            // Draw the hand cursor
            cursorGraphicsContext.clearRect(oldX, oldY, 100, 100);
            handCursor.setPosition(x, y);
            handCursor.render(cursorGraphicsContext);
        }

        oldX = x;
        oldY = y;
    }

    @Override
    public void onRightHandPushed(boolean rightHandIsPushed)
    {
        this.rightHandIsPushed = rightHandIsPushed;

        if (rightHandIsPushed)
        {
            for (TextureButton textureButton : this.textureButtons)
            {
                if (textureButton.getImageView().getBoundsInParent().intersects(handCursor.getPositionX(),
                        handCursor.getPositionY(), handCursor.getWidth(), handCursor.getHeight()))
                {
                    currentlySelectedTextureButton = textureButton;
                    break;
                }
            }
        }
    }
}