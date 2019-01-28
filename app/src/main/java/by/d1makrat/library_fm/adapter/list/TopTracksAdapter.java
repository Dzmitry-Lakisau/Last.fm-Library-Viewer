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
import by.d1makrat.library_fm.model.Track;

public class TopTracksAdapter extends ItemsAdapter<Track> {

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
    protected RecyclerView.ViewHolder createItemWithOffsetViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_with_offset_ranked, parent, false);

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
        private final TextView playCountTextView;
        private final TextView rankTextView;
        private final ImageView coverImgView;

        TopTrackViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            trackTextView = pView.findViewById(R.id.primaryField_textView);
            artistTextView = pView.findViewById(R.id.secondaryField_textView);
            playCountTextView = pView.findViewById(R.id.playCount_textView);
            rankTextView = pView.findViewById(R.id.rank);
            coverImgView = pView.findViewById(R.id.cover_ImgView);
        }

        private void bind(Track topTrack, Drawable pPlaceholderDrawable){

            trackTextView.setText(topTrack.getTitle());
            artistTextView.setText(topTrack.getArtistName());
            playCountTextView.setText(playCountTextView.getContext().getResources().getQuantityString(R.plurals.scrobbles_count, topTrack.getPlayCount(), topTrack.getPlayCount()));
            rankTextView.setText(topTrack.getRank());

            String imageUri = BuildConfig.DEBUG ? topTrack.getImageUrl() : null;
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(coverImgView);
        }
    }
}
