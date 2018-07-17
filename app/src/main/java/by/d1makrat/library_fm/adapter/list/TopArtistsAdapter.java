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
import by.d1makrat.library_fm.model.TopArtist;

public class TopArtistsAdapter extends ItemsAdapter<TopArtist> {

    public TopArtistsAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_ranked, parent, false);

        return new TopArtistViewHolder(view, this);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TopArtistViewHolder holder = (TopArtistViewHolder) viewHolder;
        holder.bind(getItem(position), mPlaceholderDrawable);
    }

    private static class TopArtistViewHolder extends LongClickableViewHolder {

        private final TextView artistTextView;
        private final TextView playcountTextView;
        private final TextView rankTextView;
        private final ImageView albumartImgView;

        TopArtistViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            artistTextView = pView.findViewById(R.id.primaryField_textView);
            playcountTextView = pView.findViewById(R.id.playcount_textView);
            rankTextView = pView.findViewById(R.id.rank);
            albumartImgView = pView.findViewById(R.id.albumart);
        }

        private void bind(TopArtist topArtist, Drawable pPlaceholderDrawable){

            artistTextView.setText(topArtist.getName());
            playcountTextView.setText(AppContext.getInstance().getString(R.string.top_scrobbles_count, topArtist.getPlaycount()));
            rankTextView.setText(topArtist.getRank());

            String imageUri = topArtist.getImageUri();
            Malevich.INSTANCE.load(null).instead(pPlaceholderDrawable).into(albumartImgView);
        }
    }
}
