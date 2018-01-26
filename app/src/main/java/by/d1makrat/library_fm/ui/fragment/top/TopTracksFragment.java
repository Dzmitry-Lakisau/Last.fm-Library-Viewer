package by.d1makrat.library_fm.ui.fragment.top;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.TopTracksAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.TopTrack;
import by.d1makrat.library_fm.operation.TopTracksOperation;

public class TopTracksFragment extends TopItemsFragment<TopTrack> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_tracks);

        return rootView;
    }

    @Override
    protected TopTracksAdapter createAdapter() {
        return new TopTracksAdapter(getActivity().getLayoutInflater(), ContextCompat.getDrawable(getActivity(), R.drawable.ic_person));
    }

    @Override
    public void performOperation() {
        TopTracksOperation topTracksOperation = new TopTracksOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<TopTrack> getItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(topTracksOperation);
    }
}