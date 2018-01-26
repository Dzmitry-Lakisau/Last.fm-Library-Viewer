package by.d1makrat.library_fm.ui.fragment.top;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.TopAlbumsAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.TopAlbum;
import by.d1makrat.library_fm.operation.TopAlbumsOperation;

public class TopAlbumsFragment extends TopItemsFragment<TopAlbum> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_albums);

        return rootView;
    }

    @Override
    protected TopAlbumsAdapter createAdapter() {
        return new TopAlbumsAdapter(getActivity().getLayoutInflater(), ContextCompat.getDrawable(getActivity(), R.drawable.img_vinyl));
    }

    @Override
    public void performOperation() {
        TopAlbumsOperation topAlbumsOperation = new TopAlbumsOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<TopAlbum> getTopItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getTopItemsAsyncTask.execute(topAlbumsOperation);
    }
}