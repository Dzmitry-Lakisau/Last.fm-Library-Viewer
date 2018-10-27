package by.d1makrat.library_fm.adapter.list;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.BuildConfig;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.Artist;

public class TopArtistsAdapter extends ItemsAdapter<Artist> {

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
        private final TextView playCountTextView;
        private final TextView rankTextView;
        private final ImageView coverImgView;

        TopArtistViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            artistTextView = pView.findViewById(R.id.primaryField_textView);
            playCountTextView = pView.findViewById(R.id.playCount_textView);
            rankTextView = pView.findViewById(R.id.rank);
            coverImgView = pView.findViewById(R.id.cover_ImgView);
        }

        private void bind(Artist topArtist, Drawable pPlaceholderDrawable){

            artistTextView.setText(topArtist.getName());
            playCountTextView.setText(AppContext.getInstance().getString(R.string.top_scrobbles_count, topArtist.getPlayCount()));
            rankTextView.setText(topArtist.getRank());

            String imageUri = BuildConfig.DEBUG ? topArtist.getImageUrl() : null;
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(coverImgView);
        }
    }
}
