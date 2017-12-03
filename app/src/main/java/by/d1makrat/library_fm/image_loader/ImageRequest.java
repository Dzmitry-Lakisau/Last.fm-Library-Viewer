package by.d1makrat.library_fm.image_loader;


import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

class ImageRequest {
    String url;
    WeakReference<ImageView> target;
    Drawable placeholderDrawable;
    int width;
    int height;

    private ImageRequest(Builder builder) {
        url = builder.url;
        target = builder.target;
        placeholderDrawable = builder.placeholderDrawable;
    }

    public static final class Builder {
        private final Malevich malevich;
        private String url;
        private WeakReference<ImageView> target;
        public Drawable placeholderDrawable;

        Builder(Malevich malevich) {
            this.malevich = malevich;
        }

        Builder load(String val) {
            url = val;
            return this;
        }

        public void into(ImageView val) {
            target = new WeakReference<>(val);
            malevich.enqueue(this.build());
        }

        public Builder instead(Drawable pPlaceholderDrawable) {
            placeholderDrawable = pPlaceholderDrawable;
//            malevich.enqueue(this.build());
            return this;
        }

        ImageRequest build() {
            return new ImageRequest(this);
        }
    }
}
