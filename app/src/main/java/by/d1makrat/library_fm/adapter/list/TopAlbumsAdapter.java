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
import by.d1makrat.library_fm.model.TopAlbum;

public class TopAlbumsAdapter extends ItemsAdapter<TopAlbum> {

    public TopAlbumsAdapter(LayoutInflater pLayoutInflater, Drawable pPlaceholderDrawable) {
        mLayoutInflater = pLayoutInflater;
        mPlaceholderDrawable = pPlaceholderDrawable;
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.item_ranked, parent, false);

        return new TopAlbumViewHolder(view, this);
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TopAlbumViewHolder holder = (TopAlbumViewHolder) viewHolder;
        holder.bind(getItem(position), mPlaceholderDrawable);
    }

    private static class TopAlbumViewHolder extends LongClickableViewHolder {

        private final TextView albumTextView;
        private final TextView artistTextView;
        private final TextView playcountTextView;
        private final TextView rankTextView;
        private final ImageView albumartImgView;

        TopAlbumViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            albumTextView = pView.findViewById(R.id.artistName_textView);
            artistTextView = pView.findViewById(R.id.secondaryField_textView);
            playcountTextView = pView.findViewById(R.id.playcount_textView);
            rankTextView = pView.findViewById(R.id.rank);
            albumartImgView = pView.findViewById(R.id.albumart);
        }

        private void bind(TopAlbum topAlbum, Drawable pPlaceholderDrawable){

            albumTextView.setText(topAlbum.getTitle());
            artistTextView.setText(topAlbum.getArtistName());
            playcountTextView.setText(AppContext.getInstance().getString(R.string.top_scrobbles_count, topAlbum.getPlaycount()));
            rankTextView.setText(topAlbum.getRank());

            String imageUri = topAlbum.getImageUri();
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(albumartImgView);
        }
    }
}
