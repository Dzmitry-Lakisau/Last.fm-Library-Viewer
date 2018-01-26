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
import by.d1makrat.library_fm.model.Artist;

public class SearchArtistsAdapter extends ItemsAdapter<Artist>{

    private final View.OnClickListener mClickListener;

    public SearchArtistsAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable, View.OnClickListener pClickListener) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
        mClickListener = pClickListener;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_artist, parent, false);

        return new ArtistViewHolder(view, mClickListener);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ArtistViewHolder holder = (ArtistViewHolder) viewHolder;
        holder.bind(getItem(position), mPlaceholderDrawable);
    }
    
    private static class ArtistViewHolder extends RecyclerView.ViewHolder{

        private final TextView artistTextView;
        private final TextView playcountTextView;
        private final ImageView artistImgView;

        ArtistViewHolder(View pView, View.OnClickListener pClickListener) {
            super(pView);

            artistTextView = pView.findViewById(R.id.artistName_textView);
            playcountTextView = pView.findViewById(R.id.playcount_textView);
            artistImgView = pView.findViewById(R.id.artistImage_ImgView);

            pView.setOnClickListener(pClickListener);
        }

        private void bind(Artist artist, Drawable pPlaceholderDrawable){

            artistTextView.setText(artist.getName());
            playcountTextView.setText(String.format(AppContext.getInstance().getString(R.string.listeners_count), artist.getListenersCount()));

            String imageUri = artist.getImageUri();
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(artistImgView);//TODO make icon larger
        }
    }
}