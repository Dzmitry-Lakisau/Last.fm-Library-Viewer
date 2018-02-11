package by.d1makrat.library_fm.adapter.list;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.TopTrack;

public class TopTracksAdapter extends ItemsAdapter<TopTrack> {

    public TopTracksAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_ranked, parent, false);

        return new TopTrackViewHolder(view, this);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TopTrackViewHolder holder = (TopTrackViewHolder) viewHolder;
        holder.bind(getItem(position), mPlaceholderDrawable);
    }

    private static class TopTrackViewHolder extends LongClickableViewHolder {

        private final TextView trackTextView;
        private final TextView artistTextView;
        private final TextView playcountTextView;
        private final TextView rankTextView;
        private final ImageView albumartImgView;

        TopTrackViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            trackTextView = pView.findViewById(R.id.artistName_textView);
            artistTextView = pView.findViewById(R.id.secondaryField_textView);
            playcountTextView = pView.findViewById(R.id.playcount_textView);
            rankTextView = pView.findViewById(R.id.rank);
            albumartImgView = pView.findViewById(R.id.albumart);
        }

        private void bind(TopTrack topTrack, Drawable pPlaceholderDrawable){

            trackTextView.setText(topTrack.getTitle());
            artistTextView.setText(topTrack.getArtistName());
            playcountTextView.setText(AppContext.getInstance().getString(R.string.top_scrobbles_count, topTrack.getPlaycount()));
            rankTextView.setText(topTrack.getRank());

            String imageUri = topTrack.getImageUri();
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(albumartImgView);
        }
    }
}
