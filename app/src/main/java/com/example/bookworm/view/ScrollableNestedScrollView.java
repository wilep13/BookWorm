package com.example.bookworm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.widget.NestedScrollView;

/**
 * NestedScrollView that yields horizontal touch events to child views (e.g. ViewPager2)
 * instead of intercepting them for its own vertical scroll. Avoids swipe-carousel conflicts.
 */
public class ScrollableNestedScrollView extends NestedScrollView {

    private float startX, startY;

    public ScrollableNestedScrollView(Context context) {
        super(context);
    }

    public ScrollableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX() - startX);
                float dy = Math.abs(ev.getY() - startY);
                if (dx > dy) return false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
