package by.d1makrat.library_fm.adapter.list;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.Scrobble;

public class ScrobblesAdapter extends ItemsAdapter<Scrobble> {

//    private final View.OnLongClickListener mLongClickListener;

    public ScrobblesAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable) {
        super();
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
//        mLongClickListener = pLongClickListener;
//        mLongClickListener = (LongClickListener) this;
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

    private static class ScrobbleViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        private final TextView trackTextView;
        private final TextView artistTextView;
        private final TextView albumTextView;
        private final TextView timestampTextView;
        private final ImageView albumartImgView;

        private LongClickListener mLongClickListener;

        ScrobbleViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView);

            trackTextView = pView.findViewById(R.id.track);
            artistTextView = pView.findViewById(R.id.artist);
            albumTextView = pView.findViewById(R.id.album);
            timestampTextView = pView.findViewById(R.id.timestamp);
            albumartImgView = pView.findViewById(R.id.albumart);

            pView.setOnLongClickListener(this);
            mLongClickListener = pLongClickListener;
        }

        private void bind(Scrobble scrobble, Drawable pPlaceholderDrawable){

            trackTextView.setText(scrobble.getTrackTitle());
            artistTextView.setText(scrobble.getArtist());
            albumTextView.setText(scrobble.getAlbum());
            timestampTextView.setText(scrobble.getFormattedDate());

            String imageUri = scrobble.getImageUri();
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(albumartImgView);
        }

        @Override
        public boolean onLongClick(View v) {
            mLongClickListener.onItemLongClick(getLayoutPosition());
            return false;
        }
    }
}