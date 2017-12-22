import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

abstract class Button
{
    private int id;
    ImageView imageView;

    void setId(int id)
    {
        this.id = id;
    }

    int getId()
    {
        return this.id;
    }

    ImageView getImageView()
    {
        return imageView;
    }

}
