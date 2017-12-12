import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;

public class Cursor
{
    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;

    Cursor()
    {
        positionX = 0;
        positionY = 0;
    }

    private void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    void setImage(FileInputStream fileInputStream)
    {
        Image i = new Image(fileInputStream);
        setImage(i);
    }

    void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    double getWidth()
    {
        return this.width;
    }

    double getHeight()
    {
        return this.height;
    }

    double getPositionX()
    {
        return this.positionX;
    }

    double getPositionY()
    {
        return this.positionY;
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    void render(GraphicsContext gc)
    {
        gc.drawImage(image, positionX, positionY);
    }
}