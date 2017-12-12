import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class PictureButton extends Button
{
    private Image image;

    PictureButton(ImageView imageView, Image image)
    {
        super.imageView = imageView;
        this.image = image;
    }

    Image getImage()
    {
        return this.image;
    }
}