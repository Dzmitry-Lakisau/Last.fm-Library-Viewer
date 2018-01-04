package by.d1makrat.library_fm.adapter.list;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.Scrobble;

//TODO Create base adapter for Lists
public class ScrobblesListAdapter extends BaseAdapter {

    private final Drawable mPlaceholderDrawable;
    private LayoutInflater mLayoutInflater;
    private List<Scrobble> mScrobbles;

    public ScrobblesListAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable, List<Scrobble> pScrobbles) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
        mScrobbles = pScrobbles;
    }

    @Override
    public int getCount() {
        return mScrobbles.size();
    }

    @Override
    public Scrobble getItem(int position) {
        return mScrobbles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;//better is "unixtime + artist + title"
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView == null) {

//            LayoutInflater mLayoutInflater = ((Activity) mLayoutInflater).getLayoutInflater();// LayoutInflater.from(mLayoutInflater);
            convertView = mLayoutInflater.inflate(R.layout.list_item, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.track_txt = convertView.findViewById(R.id.track);
            viewHolder.artist_txt = convertView.findViewById(R.id.artist);
            viewHolder.album_txt = convertView.findViewById(R.id.album);
            viewHolder.timestamp_txt = convertView.findViewById(R.id.timestamp);
            viewHolder.albumart_imgView = convertView.findViewById(R.id.albumart);

            convertView.setTag(viewHolder);
//            Log.d("MYTAG", "viewholder created for " + scrobble.getArtist()+" - "+ scrobble.getTrackTitle());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            Log.d("MYTAG", "viewholder found for " + scrobble.getArtist()+" - "+ scrobble.getTrackTitle());
        }

        final Scrobble scrobble = mScrobbles.get(position);

        viewHolder.track_txt.setText(scrobble.getTrackTitle());
        viewHolder.artist_txt.setText(scrobble.getArtist());
        viewHolder.album_txt.setText(scrobble.getAlbum());
        viewHolder.timestamp_txt.setText(scrobble.getFormattedDate());

        String imageUri = scrobble.getImageUri();
        ImageView imageView = viewHolder.albumart_imgView;

        if (viewHolder.albumart_imgView.getTag() == null || !viewHolder.albumart_imgView.getTag().equals(imageUri)) {

            Malevich.INSTANCE.load(imageUri).instead(mPlaceholderDrawable).into(imageView);//TODO images setting up multiple times?
//            Log.d("MYTAG", "setting image for " + scrobble.getArtist()+" - "+ scrobble.getTrackTitle());

            viewHolder.albumart_imgView.setTag(imageUri);
        }

        return convertView;
    }

    static class ViewHolder {
        //TODO naming
        TextView track_txt;
        TextView artist_txt;
        TextView album_txt;
        TextView timestamp_txt;
        ImageView albumart_imgView;
    }
}
