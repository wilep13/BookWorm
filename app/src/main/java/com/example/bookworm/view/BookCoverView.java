package com.example.bookworm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.bookworm.R;
import com.example.bookworm.model.Book;

import java.util.HashMap;
import java.util.Map;

public class BookCoverView extends View {

    // Figma: book body image is positioned as h=118.03%, w=186.21%, left=-46.55%, top=-8.74%
    // inside a 116×183 card with overflow:hidden — visible crop of the square image:
    private static final float SRC_L = 0.250f;   // 54 / 216
    private static final float SRC_T = 0.074f;   // 16 / 216
    private static final float SRC_R = 0.787f;   // 170 / 216
    private static final float SRC_B = 0.921f;   // 199 / 216

    // Pixel-measured white face area inside the 4096x4096 book body image:
    // left=1407, top=420, right=3105, bottom=2937 (relative to SRC crop 1024,303→3224,3773)
    private static final float FACE_L = 0.1741f;
    private static final float FACE_T = 0.0337f;
    private static final float FACE_R = 0.0545f;
    private static final float FACE_B = 0.2409f;

    private static Bitmap sBodyFicBitmap;
    private static Bitmap sBodyNonficBitmap;
    private static final Map<Integer, Bitmap> sCoverCache = new HashMap<>();

    // Edit-mode only — decoded once, shared across all preview instances
    private static Bitmap sEditBodyBitmap;
    private static Bitmap sEditCoverBitmap;

    private Bitmap bodyBitmap;
    private Bitmap coverBitmap;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public BookCoverView(Context context) { super(context); }
    public BookCoverView(Context context, AttributeSet attrs) { super(context, attrs); }
    public BookCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBook(Book book) {
        if ("Fiction".equals(book.getCategory())) {
            if (sBodyFicBitmap == null)
                sBodyFicBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.book_body_fic);
            bodyBitmap = sBodyFicBitmap;
        } else {
            if (sBodyNonficBitmap == null)
                sBodyNonficBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.book_body_nonfic);
            bodyBitmap = sBodyNonficBitmap;
        }
        int resId = book.getCoverResId();
        if (resId != 0) {
            if (!sCoverCache.containsKey(resId)) {
                sCoverCache.put(resId, BitmapFactory.decodeResource(getResources(), resId));
            }
            coverBitmap = sCoverCache.get(resId);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        if (w == 0) w = (int)(133 * getResources().getDisplayMetrics().density);
        setMeasuredDimension(w, Math.round(w * 183f / 116f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            int w = getWidth(), h = getHeight();
            if (w == 0 || h == 0) { canvas.drawColor(0xFFC08552); return; }
            // Body: use BitmapFactory (sampled down for speed)
            if (sEditBodyBitmap == null) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 4;
                sEditBodyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.book_body_fic, opts);
            }
            if (sEditBodyBitmap != null) {
                int bW = sEditBodyBitmap.getWidth(), bH = sEditBodyBitmap.getHeight();
                Rect src = new Rect(
                        Math.round(bW * SRC_L), Math.round(bH * SRC_T),
                        Math.round(bW * SRC_R), Math.round(bH * SRC_B));
                canvas.drawBitmap(sEditBodyBitmap, src, new RectF(0, 0, w, h), paint);
            } else {
                canvas.drawColor(0xFFC08552);
            }
            // Cover art: use Drawable API — more reliable in Android Studio layout preview
            android.graphics.drawable.Drawable coverD =
                    getContext().getDrawable(R.drawable.cover_art_gatsby);
            if (coverD != null) {
                coverD.setBounds(
                        Math.round(w * FACE_L), Math.round(h * FACE_T),
                        Math.round(w * (1f - FACE_R)), Math.round(h * (1f - FACE_B)));
                coverD.draw(canvas);
            }
            return;
        }
        if (bodyBitmap == null) return;
        int w = getWidth();
        int h = getHeight();

        // Crop book body to the visible portion defined by Figma's overflow:hidden clip
        int bW = bodyBitmap.getWidth();
        int bH = bodyBitmap.getHeight();
        Rect src = new Rect(
                Math.round(bW * SRC_L), Math.round(bH * SRC_T),
                Math.round(bW * SRC_R), Math.round(bH * SRC_B));
        canvas.drawBitmap(bodyBitmap, src, new RectF(0, 0, w, h), paint);

        // Overlay cover art on the book face
        if (coverBitmap != null) {
            RectF face = new RectF(
                    w * FACE_L,
                    h * FACE_T,
                    w * (1f - FACE_R),
                    h * (1f - FACE_B));
            canvas.drawBitmap(coverBitmap, null, face, paint);
        }
    }
}
