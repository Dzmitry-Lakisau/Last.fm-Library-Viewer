package by.d1makrat.library_fm.adapter.list;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.d1makrat.library_fm.BuildConfig;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.Scrobble;

public class ScrobblesAdapter extends ItemsAdapter<Scrobble> {

    public ScrobblesAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_scrobble, parent, false);

        return new ScrobbleViewHolder(view, this);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ScrobbleViewHolder holder = (ScrobbleViewHolder) viewHolder;
        holder.bind(getItem(position), mPlaceholderDrawable);
    }

    private static class ScrobbleViewHolder extends LongClickableViewHolder {

        private final TextView trackTextView;
        private final TextView artistTextView;
        private final TextView albumTextView;
        private final TextView timestampTextView;
        private final ImageView coverImgView;

        ScrobbleViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            trackTextView = pView.findViewById(R.id.track);
            artistTextView = pView.findViewById(R.id.artist);
            albumTextView = pView.findViewById(R.id.album);
            timestampTextView = pView.findViewById(R.id.timestamp);
            coverImgView = pView.findViewById(R.id.cover_ImgView);
        }

        private void bind(Scrobble scrobble, Drawable pPlaceholderDrawable){

            trackTextView.setText(scrobble.getTrackTitle());
            artistTextView.setText(scrobble.getArtist());
            albumTextView.setText(scrobble.getAlbum());
            timestampTextView.setText(scrobble.getFormattedDate());

            String imageUri = BuildConfig.DEBUG ? scrobble.getImageUrl() : null;
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(coverImgView);
        }
    }
}
