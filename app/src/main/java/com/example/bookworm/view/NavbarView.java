package com.example.bookworm.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.example.bookworm.R;

public class NavbarView extends View {

    public static final int SLOT_HOME   = 0;
    public static final int SLOT_BOOKS  = 1;
    public static final int SLOT_STORES = 2;
    public static final int SLOT_LOGOUT = 3;

    // Slot center X positions in dp (from Figma)
    private static final float[] SLOT_DP = { 69f, 158f, 246f, 342f };

    private final float density;
    private int   activeSlot = SLOT_HOME;
    private float bubbleDp   = SLOT_DP[SLOT_HOME];

    private final Paint barPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path  barPath     = new Path();

    private final Drawable[] icons = new Drawable[4];

    public interface OnTabClickListener { void onTabClick(int slot); }
    private OnTabClickListener listener;

    public NavbarView(Context c) { this(c, null); }
    public NavbarView(Context c, AttributeSet a) { this(c, a, 0); }
    public NavbarView(Context c, AttributeSet a, int def) {
        super(c, a, def);
        density = c.getResources().getDisplayMetrics().density;
        barPaint.setColor(0xFFC08552);    // color_primary_1
        bubblePaint.setColor(0xFF7C3900); // dark bubble
        int[] res = {
            R.drawable.ic_nav_home_v2, R.drawable.ic_nav_books_v2,
            R.drawable.ic_nav_cart,    R.drawable.ic_nav_logout
        };
        for (int i = 0; i < 4; i++) icons[i] = ContextCompat.getDrawable(c, res[i]);
    }

    public void setOnTabClickListener(OnTabClickListener l) { listener = l; }

    public void setSlot(int slot) {
        activeSlot = slot;
        bubbleDp   = SLOT_DP[slot];
        invalidate();
    }

    public void animateToSlot(int slot) {
        if (slot == activeSlot) return;
        float from = bubbleDp, to = SLOT_DP[slot];
        activeSlot = slot;
        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        va.setDuration(320);
        va.setInterpolator(new FastOutSlowInInterpolator());
        va.addUpdateListener(a -> { bubbleDp = (float) a.getAnimatedValue(); invalidate(); });
        va.start();
    }

    @Override
    protected void onMeasure(int ws, int hs) {
        setMeasuredDimension(MeasureSpec.getSize(ws), (int)(92 * density));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) { canvas.drawColor(0xFFC08552); return; }
        float d  = density;
        float cx = bubbleDp * d;

        buildBarPath(cx, d);
        canvas.drawPath(barPath, barPaint);

        // Bubble circle (dark brown raised disc)
        canvas.drawCircle(cx, 24.5f * d, 24.5f * d, bubblePaint);

        // Icons: active slides with bubble, inactive at fixed positions
        for (int i = 0; i < 4; i++) {
            if (icons[i] == null) continue;
            boolean active = (i == activeSlot);
            float icx = active ? cx : SLOT_DP[i] * d;
            float icy = active ? 10f * d : 33f * d;
            icons[i].setTint(active ? 0xFFFFF8F0 : 0xFF553522);
            icons[i].setBounds((int)(icx - 12*d), (int)icy,
                               (int)(icx + 12*d), (int)(icy + 24*d));
            icons[i].draw(canvas);
        }
    }

    // Draws caramel bar with a bezier scoop at bubble center cx (px).
    // Path derived from Figma bg_nav_pill_* SVG with parameterized cx.
    private void buildBarPath(float cx, float d) {
        float r   = 10 * d;
        float w   = getWidth();
        float top = 10 * d;
        float h   = 92 * d;

        barPath.reset();
        barPath.moveTo(0, 20 * d);
        // Top-left corner
        barPath.arcTo(new RectF(0, top, r*2, top+r*2), 180, 90, false);
        // Top edge → scoop left entry
        barPath.lineTo(cx - 39.5f*d, top);
        // Left scoop bezier (down into trough)
        barPath.cubicTo(cx - 27.5f*d, 15.5f*d,
                        cx - 39f*d,   55.5f*d,
                        cx - 0.5f*d,  55.5f*d);
        // Right scoop bezier (back up)
        barPath.cubicTo(cx + 38f*d,   55.5f*d,
                        cx + 25f*d,   top,
                        cx + 39.5f*d, top);
        // Top edge → top-right corner
        barPath.lineTo(w - r*2, top);
        barPath.arcTo(new RectF(w-r*2, top, w, top+r*2), 270, 90, false);
        // Right edge down
        barPath.lineTo(w, 82*d);
        // Bottom-right corner
        barPath.arcTo(new RectF(w-r*2, 82*d, w, h), 0, 90, false);
        // Bottom edge
        barPath.lineTo(r, h);
        // Bottom-left corner
        barPath.arcTo(new RectF(0, 82*d, r*2, h), 90, 90, false);
        barPath.close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            float xDp = e.getX() / density;
            int nearest = 0;
            float minDist = Float.MAX_VALUE;
            for (int i = 0; i < 4; i++) {
                float dist = Math.abs(xDp - SLOT_DP[i]);
                if (dist < minDist) { minDist = dist; nearest = i; }
            }
            if (listener != null) { listener.onTabClick(nearest); return true; }
        }
        return super.onTouchEvent(e);
    }
}
