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

public class SearchArtistsAdapter extends ItemsAdapter<Artist>{

    public SearchArtistsAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_artist, parent, false);

        return new ArtistViewHolder(view, this);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ArtistViewHolder holder = (ArtistViewHolder) viewHolder;
        holder.bind(getItem(position), mPlaceholderDrawable);
    }
    
    private static class ArtistViewHolder extends LongClickableViewHolder {

        private final TextView artistNameTextView;
        private final TextView listenersCountTextView;
        private final ImageView artistImgView;

        ArtistViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            artistNameTextView = pView.findViewById(R.id.artistName_textView);
            listenersCountTextView = pView.findViewById(R.id.listenersCount_textView);
            artistImgView = pView.findViewById(R.id.artistImage_ImgView);
        }

        private void bind(Artist artist, Drawable pPlaceholderDrawable){

            artistNameTextView.setText(artist.getName());
            listenersCountTextView.setText(AppContext.getInstance().getResources().getQuantityString(R.plurals.listeners_count, artist.getListenersCount(), artist.getListenersCount()));

            String imageUri = BuildConfig.DEBUG ? artist.getImageUrl() : null;
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(artistImgView);
        }
    }
}
