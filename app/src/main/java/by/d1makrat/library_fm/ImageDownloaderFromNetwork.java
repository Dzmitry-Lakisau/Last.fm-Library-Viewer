package by.d1makrat.library_fm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ImageDownloaderFromNetwork {

    private Context mContext;
    private String mPathToCache;
    private String mResolution;

    public ImageDownloaderFromNetwork(Context pContext){

        mContext = pContext;

        AppSettings appSettings = new AppSettings(mContext);
        mPathToCache = appSettings.getCacheDir();
        mResolution = appSettings.getResolution();
    }

    public void download(Scrobble scrobble) throws IOException{

//        boolean exception = false;

        String filename = null;
        FileOutputStream out = null;
        Bitmap image;

        File folder = createFolderForImage();
        if (!scrobble.getImageUriBySize(mResolution).equals("")) {

            if (scrobble.getAlbum().getAlbumTitle() != null || !scrobble.getAlbum().getAlbumTitle().equals(""))
                filename = folder.getPath() + File.separator + scrobble.getArtist().getArtistName().replaceAll("[\\\\/:*?\"<>|]", "_") + " - " + scrobble.getAlbum().getAlbumTitle().replaceAll("[\\\\/:*?\"<>|]", "_") + ".png";
            else
                filename = folder.getPath() + File.separator + scrobble.getArtist().getArtistName().replaceAll("[\\\\/:*?\"<>|]", "_") + ".png";

            if (!(new File(filename).exists())) {
                try {
                    java.net.URL newurl = new URL(scrobble.getImageUriBySize(mResolution));
                    image = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                    out = new FileOutputStream(filename);
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                }
                catch (Exception e){
//                    exception = true;
//                    //FirebaseCrash.report(e);
//                    e.printStackTrace();
                    File file = new File(filename);
                    boolean deleted = false;
                    while (!deleted) {
                       deleted = file.delete();//TODO
                    }
                    throw e;
                }
                finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    }
                    catch (IOException e) {
//                        //FirebaseCrash.report(e);
//                        e.printStackTrace();
////                        exception = 3;
                        throw e;
                    }
                }
            }
        } else {
            AppSettings appSettings = new AppSettings(mContext);
            filename = appSettings.getPathToBlankAlbumart();
        }
        scrobble.setImageUriBySize(mResolution, filename);
//        return filename;
    }

    private File createFolderForImage(){

        File folder = new File(mPathToCache + File.separator + mResolution + File.separator + "Albums");
        if (!folder.exists()) {
            boolean folderCreated = false;
            while (!folderCreated) {
                folderCreated = folder.mkdirs();//TODO
            }
        }
        return folder;
    }
}
