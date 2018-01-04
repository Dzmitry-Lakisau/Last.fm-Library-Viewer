package by.d1makrat.library_fm.adapter.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.Scrobble;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Scrobble> mScrobbles;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //TODO naming
        TextView track_txt;
        TextView artist_txt;
        TextView album_txt;
        TextView timestamp_txt;
        ImageView albumart_imgView;

        ViewHolder(View pView) {
            super(pView);

            track_txt = pView.findViewById(R.id.track);
            artist_txt = pView.findViewById(R.id.artist);
            album_txt = pView.findViewById(R.id.album);
            timestamp_txt = pView.findViewById(R.id.timestamp);
            albumart_imgView = pView.findViewById(R.id.albumart);
        }
    }

    public RecyclerViewAdapter(List<Scrobble> pScrobbles) {
        mScrobbles = pScrobbles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final Scrobble scrobble = mScrobbles.get(position);

        viewHolder.track_txt.setText(scrobble.getTrackTitle());
        viewHolder.artist_txt.setText(scrobble.getArtist());
        viewHolder.album_txt.setText(scrobble.getAlbum());
        viewHolder.timestamp_txt.setText(scrobble.getFormattedDate());

        String imageUri = scrobble.getImageUri();
        ImageView imageView = viewHolder.albumart_imgView;
        if (imageUri == null)
            imageView.setImageResource(R.drawable.default_albumart);
        else
//            ((ImageView) view.findViewById(R.id.albumart)).setImageURI(Uri.parse(imageUri));
        Malevich.INSTANCE.load(imageUri).into(imageView);
    }

    @Override
    public int getItemCount() {
        return mScrobbles.size();
    }
}