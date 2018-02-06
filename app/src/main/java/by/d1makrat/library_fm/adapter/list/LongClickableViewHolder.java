package by.d1makrat.library_fm.adapter.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LongClickableViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    private LongClickListener mLongClickListener;

    LongClickableViewHolder(View pView, LongClickListener pLongClickListener) {
        super(pView);

        pView.setOnLongClickListener(this);
        mLongClickListener = pLongClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        mLongClickListener.onItemLongClick(getLayoutPosition());
        return false;
    }
}
