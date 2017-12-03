package by.d1makrat.library_fm.ui;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {

        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int size = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(size, size);
        }
}
