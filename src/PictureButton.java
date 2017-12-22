import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class PictureButton extends Button {
    private Image image;

    PictureButton(ImageView imageView, Image image) {
        super.imageView = imageView;
        this.image = image;
    }

    Image getImage() {
        return image;
    }

    private Rectangle2D getBoundary() {
        return new Rectangle2D(1166, imageView.getBoundsInParent().getMinY(), 200, 100);
    }

    boolean intersects(Cursor cursor) {
        return cursor.getBoundary().intersects(this.getBoundary());
    }
}