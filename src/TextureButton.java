import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

class TextureButton
{
    private ImageView imageView;
    private int textureId;

    TextureButton(ImageView imageView)
    {
        this.imageView = imageView;
    }

    void setTextureId(int textureId)
    {
        this.textureId = textureId;
    }

    int getTextureId()
    {
        return this.textureId;
    }

    ImageView getImageView()
    {
        return this.imageView;
    }
}