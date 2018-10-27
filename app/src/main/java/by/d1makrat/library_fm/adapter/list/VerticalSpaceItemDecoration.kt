package by.d1makrat.library_fm.adapter.list

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R

class VerticalSpaceItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemPosition == 0) {
            if (parent.getChildViewHolder(view) is LongClickableViewHolder){
                outRect.top = AppContext.getInstance().resources.getDimension(R.dimen.item_margin).toInt()
            }
        }
    }
}
