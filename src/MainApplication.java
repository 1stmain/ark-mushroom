import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
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
    private Image textureImage1;
    private int bubbleFrameCount = 0;
    private Canvas textureCanvas;
    private boolean rightHandIsPushed = false;
    private ArrayList<TextureButton> textureButtons = new ArrayList<>();
    private ArrayList<PictureButton> pictureButtons = new ArrayList<>();
    private Button currentlySelectedButton = null;

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
        setPictureButtons();
        for (PictureButton pictureButton : this.pictureButtons)
        {
            pictureVBox.getChildren().add(pictureButton.getImageView());
        }

        setBlackShadowToAllButtons();

        AnchorPane anchorPane = new AnchorPane(textureVBox, pictureVBox);
        AnchorPane.setLeftAnchor(textureVBox, 0.0);
        AnchorPane.setRightAnchor(pictureVBox, 0.0);

        Canvas cursorCanvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
        cursorGraphicsContext = cursorCanvas.getGraphicsContext2D();

        this.textureCanvas = new Canvas(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
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

        initialiseTextures();
    }

    private void setTextureButtons()
    {
        try
        {
            Image texture1 = new Image(new FileInputStream("images\\dots_texture.png"));
            ImageView textureImageView1 = new ImageView(texture1);
            TextureButton textureButton1 = new TextureButton(textureImageView1);
            textureButton1.setId(1);

            Image texture2 = new Image(new FileInputStream("images\\texture2.jpg"));
            ImageView textureImageView2 = new ImageView(texture2);
            TextureButton textureButton2 = new TextureButton(textureImageView2);
            textureButton2.setId(2);

            textureButtons.add(textureButton1);
            textureButtons.add(textureButton2);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setPictureButtons()
    {
        try
        {
            Image picture1 = new Image(new FileInputStream("images\\lotus_image.png"));
            ImageView pictureImageView1 = new ImageView(picture1);
            PictureButton pictureButton1 = new PictureButton(pictureImageView1, picture1);
            pictureButton1.setId(1);

            Image picture2 = new Image(new FileInputStream("images\\picture2.jpg"));
            ImageView pictureImageView2 = new ImageView(picture2);
            PictureButton pictureButton2 = new PictureButton(pictureImageView2, picture2);
            pictureButton2.setId(2);

            pictureButtons.add(pictureButton1);
            pictureButtons.add(pictureButton2);
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
        if (rightHandIsPushed)
        {
            // Draw the brush cursor
            cursorGraphicsContext.clearRect(oldX, oldY, 200, 100);
            brushCursor.setPosition(x, y);
            brushCursor.render(cursorGraphicsContext);

            if (currentlySelectedButton != null && currentlySelectedButton.getClass() == TextureButton.class)
            {
                if (x >= 250 && x <= 1100)
                {
                    switch (currentlySelectedButton.getId())
                    {
                        case 1:
                            bubbleFrameCount++;

                            if (bubbleFrameCount > 4)
                            {
                                textureGraphicsContext.drawImage(textureImage1, x, y, 40, 40);
                                bubbleFrameCount = 0;
                            }

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
            else if (currentlySelectedButton != null && currentlySelectedButton.getClass() == PictureButton.class)
            {
                cursorGraphicsContext.drawImage(((PictureButton) currentlySelectedButton).getImage(), x, y);
            }
        }
        else
        {
            if (currentlySelectedButton != null && currentlySelectedButton.getClass() == PictureButton.class)
            {
                if (x >= 250 && x <= 900)
                {
                    textureGraphicsContext.drawImage(((PictureButton) currentlySelectedButton).getImage(), x, y);
                    currentlySelectedButton = null;
                    setBlackShadowToAllButtons();
                }
                else
                {
                    currentlySelectedButton = null;
                    setBlackShadowToAllButtons();
                }
            }

            // Draw the hand cursor
            cursorGraphicsContext.clearRect(oldX, oldY, 200, 100);
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
                    setBlackShadowToAllButtons();
                    currentlySelectedButton = textureButton;
                    DropShadow dropShadowRed = new DropShadow(16, Color.RED);
                    currentlySelectedButton.getImageView().setEffect(dropShadowRed);
                    break;
                }
            }

            for (PictureButton pictureButton : this.pictureButtons)
            {
                if (pictureButton.intersects(handCursor))
                {
                    setBlackShadowToAllButtons();
                    currentlySelectedButton = pictureButton;
                    DropShadow dropShadowRed = new DropShadow(16, Color.RED);
                    currentlySelectedButton.getImageView().setEffect(dropShadowRed);
                    break;
                }
            }
        }
    }

    private void setBlackShadowToAllButtons()
    {
        for (TextureButton textureButton : this.textureButtons)
        {
            DropShadow dropShadowBlack = new DropShadow(16, Color.BLACK);
            textureButton.getImageView().setEffect(dropShadowBlack);
        }

        for (PictureButton pictureButton : this.pictureButtons)
        {
            DropShadow dropShadowBlack = new DropShadow(16, Color.BLACK);
            pictureButton.getImageView().setEffect(dropShadowBlack);
        }
    }

    @Override
    public void onBothHandsRaised()
    {
        System.out.println("Program complete!");
        pixelScaleAwareCanvasSnapshot(textureCanvas, 0.5);
    }

    private void initialiseTextures()
    {
        try
        {
            textureImage1 = new Image(new FileInputStream("images\\dots_texture.png"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void pixelScaleAwareCanvasSnapshot(Canvas canvas, double pixelScale)
    {
        WritableImage writableImage = new WritableImage((int) Math.rint(pixelScale * canvas.getWidth()), (int) Math.rint(pixelScale * canvas.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(pixelScale, pixelScale));


        File file = new File("CanvasImage.png");

        Platform.runLater(
                () ->
                {
                    try
                    {
                        ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(spa, writableImage), null), "png", file);
                    }
                    catch (Exception s)
                    {
                        s.printStackTrace();
                        System.exit(1);
                    }
                });
    }


    public void repeater(){
//        GridPane gridpane = new GridPane();
//        Image image = new Image(new FileInputStream("images\\kaleidoscope.jpg"));
//        ImageView imageView = new ImageView(image);
//        imageView.setFitHeight(200);
//        imageView.setFitWidth(300);
//
//        gridpane.add(imageView, 0, 0);
//        ImageView imageView1 = new ImageView(image);
//        imageView1.setFitHeight(200);
//        imageView1.setFitWidth(300);
//        gridpane.add(imageView1, 1, 0);
//        ImageView imageView2 = new ImageView(image);
//        imageView2.setFitHeight(200);
//        imageView2.setFitWidth(300);
//
//        gridpane.add(imageView2, 2, 0);
//        ImageView imageView3 = new ImageView(image);
//        imageView3.setFitHeight(200);
//        imageView3.setFitWidth(300);
//        gridpane.add(imageView3, 0, 1);
//        ImageView imageView4 = new ImageView(image);
//        imageView4.setFitHeight(200);
//        imageView4.setFitWidth(300);
//        gridpane.add(imageView4, 1, 1);
//        ImageView imageView5 = new ImageView(image);
//        imageView5.setFitHeight(200);
//        imageView5.setFitWidth(300);
//        gridpane.add(imageView5, 2, 1);
//        ImageView imageView6 = new ImageView(image);
//        imageView6.setFitHeight(200);
//        imageView6.setFitWidth(300);
//        gridpane.add(imageView6, 0, 2);
//        ImageView imageView7 = new ImageView(image);
//        imageView7.setFitHeight(200);
//        imageView7.setFitWidth(300);
//        gridpane.add(imageView7, 1, 2);
//        ImageView imageView8 = new ImageView(image);
//        imageView8.setFitHeight(200);
//        imageView8.setFitWidth(300);
//        gridpane.add(imageView8, 2, 2);
////        gridpane.getChildren().add(new ImageView(image));
//        Scene scene = new Scene(gridpane, 900, 600);
//        primaryStage.setScene(scene);
//        primaryStage.show();



    }
}