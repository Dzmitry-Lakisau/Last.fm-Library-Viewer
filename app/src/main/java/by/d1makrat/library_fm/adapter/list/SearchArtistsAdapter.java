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
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.Scrobble;

public class SearchArtistsAdapter extends BaseAdapter {

    private final Drawable mPlaceholderDrawable;
    private LayoutInflater mLayoutInflater;
    private List<Artist> artists;

    public SearchArtistsAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable, List<Artist> pArtists) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
        this.artists = pArtists;
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Scrobble getItem(int position) {
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;//better is "unixtime + artist + title"
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.ranked_list_item, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.primaryField_txt = convertView.findViewById(R.id.primaryField_textView);
            viewHolder.playcount_txt = convertView.findViewById(R.id.playcount_textView);
            viewHolder.albumart_imgView = convertView.findViewById(R.id.albumart);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Artist artist = artists.get(position);

        viewHolder.primaryField_txt.setText(artist.getName());
        viewHolder.playcount_txt.setText(String.format("%s listeners", artist.getListenersCount()));

        String imageUri = artist.getImageUri();
        ImageView imageView = viewHolder.albumart_imgView;

        if (viewHolder.albumart_imgView.getTag() == null || !viewHolder.albumart_imgView.getTag().equals(imageUri)) {

            Malevich.INSTANCE.load(imageUri).instead(mPlaceholderDrawable).into(imageView);//TODO images setting up multiple times?
            viewHolder.albumart_imgView.setTag(imageUri);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView primaryField_txt;
        TextView playcount_txt;
        ImageView albumart_imgView;
    }
}