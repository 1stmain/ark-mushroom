import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class is used to group all the characteristics of a Texture button together.
 *
 * @author Nihar Thakkar
 * @version 1.0
 * @since 07-12-2017
 */

public class Texture
{
    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;

    public Texture()
    {
        positionX = 0;
        positionY = 0;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite sprite)
    {
        return sprite.getBoundary().intersects(this.getBoundary());
    }
}