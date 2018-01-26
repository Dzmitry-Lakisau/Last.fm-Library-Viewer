package by.d1makrat.library_fm.ui.fragment.top;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.TopArtistsAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.TopArtist;
import by.d1makrat.library_fm.operation.TopArtistsOperation;

public class TopArtistsFragment extends TopItemsFragment<TopArtist> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_artists);

        return rootView;
    }

    @Override
    protected TopArtistsAdapter createAdapter() {
        return new TopArtistsAdapter(getActivity().getLayoutInflater(), ContextCompat.getDrawable(getActivity(), R.drawable.ic_person));
    }

    @Override
    public void performOperation() {
        TopArtistsOperation topArtistsOperation = new TopArtistsOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<TopArtist> getTopItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getTopItemsAsyncTask.execute(topArtistsOperation);
    }
}