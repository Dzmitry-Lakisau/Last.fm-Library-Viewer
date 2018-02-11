package by.d1makrat.library_fm.image_loader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

class ImageRequest {
    final String url;
    final WeakReference<ImageView> target;
    final Drawable placeholderDrawable;
    final Drawable onErrorDrawable;
    int width;
    int height;

    private ImageRequest(Builder builder) {
        url = builder.url;
        target = builder.target;
        placeholderDrawable = builder.placeholderDrawable;
        onErrorDrawable = builder.onErrorDrawable;
    }

    public static final class Builder {
        private final Malevich malevich;
        private String url;
        private WeakReference<ImageView> target;
        private Drawable placeholderDrawable;
        private Drawable onErrorDrawable;

        Builder(Malevich malevich) {
            this.malevich = malevich;
        }

        Builder load(String val) {
            url = val;
            return this;
        }

        public Builder instead(Drawable pPlaceholderDrawable) {
            placeholderDrawable = pPlaceholderDrawable;
            return this;
        }

        public Builder onError(Drawable pOnErrorDrawable){
            onErrorDrawable = pOnErrorDrawable;
            return this;
        }

        public void into(ImageView val) {
            target = new WeakReference<>(val);
            malevich.enqueue(this.build());
        }

        ImageRequest build() {
            return new ImageRequest(this);
        }
    }
}
