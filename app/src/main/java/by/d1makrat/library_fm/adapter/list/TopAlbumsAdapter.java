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
import by.d1makrat.library_fm.model.Album;

public class TopAlbumsAdapter extends ItemsAdapter<Album> {

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
        private final TextView playCountTextView;
        private final TextView rankTextView;
        private final ImageView coverImgView;

        TopAlbumViewHolder(View pView, LongClickListener pLongClickListener) {
            super(pView, pLongClickListener);

            albumTextView = pView.findViewById(R.id.primaryField_textView);
            artistTextView = pView.findViewById(R.id.secondaryField_textView);
            playCountTextView = pView.findViewById(R.id.playCount_textView);
            rankTextView = pView.findViewById(R.id.rank);
            coverImgView = pView.findViewById(R.id.cover_ImgView);
        }

        private void bind(Album topAlbum, Drawable pPlaceholderDrawable){

            albumTextView.setText(topAlbum.getTitle());
            artistTextView.setText(topAlbum.getArtistName());
            playCountTextView.setText(AppContext.getInstance().getResources().getQuantityString(R.plurals.scrobbles_count, topAlbum.getPlayCount(), topAlbum.getPlayCount()));
            rankTextView.setText(topAlbum.getRank());

            String imageUri = BuildConfig.DEBUG ? topAlbum.getImageUrl() : null;
            Malevich.INSTANCE.load(imageUri).instead(pPlaceholderDrawable).into(coverImgView);
        }
    }
}
