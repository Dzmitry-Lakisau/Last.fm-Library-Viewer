package by.d1makrat.library_fm.image_loader.cache;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

public interface DiskCache {

	File get(String imageUri) throws IOException;

	void save(String imageUri, Bitmap bitmap) throws IOException;

	void clear() throws IOException;
}
