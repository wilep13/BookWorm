package com.example.bookworm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.bookworm.model.Book;

public class BookCoverView extends View {

    private Book book;

    private final Paint bgPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint texPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint divPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint titlePaint  = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
    private final TextPaint authorPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);

    public BookCoverView(Context context) { super(context); }
    public BookCoverView(Context context, AttributeSet attrs) { super(context, attrs); }
    public BookCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBook(Book b) {
        book = b;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        if (w == 0) w = (int)(120 * getResources().getDisplayMetrics().density);
        setMeasuredDimension(w, Math.round(w * 1.5f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (book == null) return;

        final int w  = getWidth();
        final int h  = getHeight();
        final float density = getResources().getDisplayMetrics().density;
        final int clothColor  = Color.parseColor(book.getClothColor());
        final int accentColor = Color.parseColor(book.getAccentColor());

        // ── 1. Cloth background ──
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(clothColor);
        canvas.drawRect(0, 0, w, h, bgPaint);

        // ── 2. Cloth texture (repeating grid, 14 % opacity white) ──
        texPaint.setColor(Color.WHITE);
        texPaint.setAlpha(Math.round(0.14f * 255));
        texPaint.setStrokeWidth(1f);
        texPaint.setStyle(Paint.Style.STROKE);
        int step = Math.max(2, (int)(3 * density / 2));   // ~3px in reference
        for (int x = 0; x < w; x += step) canvas.drawLine(x, 0, x, h, texPaint);
        for (int y = 0; y < h; y += step) canvas.drawLine(0, y, w, y, texPaint);

        // ── 3. Spine shading (left edge) ──
        float spineW = Math.max(4f, w * 0.05f);
        LinearGradient spineGrad = new LinearGradient(0, 0, spineW, 0,
            Color.argb(Math.round(0.18f * 255), 255, 255, 255),
            Color.argb(Math.round(0.22f * 255), 0, 0, 0),
            Shader.TileMode.CLAMP);
        Paint spinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        spinePaint.setShader(spineGrad);
        canvas.drawRect(0, 0, spineW, h, spinePaint);

        // ── 4. Gold outer frame ──
        float pad = w * 0.085f;
        framePaint.setColor(accentColor);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(1.5f * density);
        framePaint.setAlpha(Math.round(0.85f * 255));
        canvas.drawRect(new RectF(pad, pad, w - pad, h - pad), framePaint);

        // ── 5. Gold inner frame ──
        float innerPad = pad + 3f;
        framePaint.setStrokeWidth(0.8f * density);
        framePaint.setAlpha(Math.round(0.50f * 255));
        canvas.drawRect(new RectF(innerPad, innerPad, w - innerPad, h - innerPad), framePaint);

        // ── 6. Title block ──
        float blockInset = pad + w * 0.06f;
        float blockL  = blockInset;
        float blockR  = w - blockInset;
        float blockT  = blockInset;
        float blockB  = h - blockInset;
        float blockW  = blockR - blockL;
        float blockCX = (blockL + blockR) / 2f;
        float blockCY = (blockT + blockB) / 2f;

        float titleSz  = Math.max(10.5f * density, w * 0.105f);
        float authorSz = Math.max(7f * density, w * 0.062f);
        float gap      = w * 0.05f;

        // Title paint — bold, serif feel via FakeBold
        titlePaint.setColor(accentColor);
        titlePaint.setTextSize(titleSz);
        titlePaint.setFakeBoldText(true);

        // Author paint — semibold, uppercase
        authorPaint.setColor(accentColor);
        authorPaint.setAlpha(Math.round(0.92f * 255));
        authorPaint.setTextSize(authorSz);
        authorPaint.setLetterSpacing(0.15f);

        StaticLayout titleLayout = StaticLayout.Builder
            .obtain(book.getTitle(), 0, book.getTitle().length(), titlePaint, (int) blockW)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1.1f)
            .setIncludePad(false)
            .build();

        String upperAuthor = book.getAuthor().toUpperCase();
        float authorTextW  = authorPaint.measureText(upperAuthor);

        float dividerH    = 1f;
        float dividerW    = blockW * 0.4f;
        float marginExtra = w * 0.02f;

        float totalContentH = dividerH + gap + titleLayout.getHeight()
                            + gap + dividerH + gap + authorSz + marginExtra;
        float startY = blockCY - totalContentH / 2f;

        // Divider paint
        divPaint.setColor(accentColor);
        divPaint.setAlpha(Math.round(0.70f * 255));
        divPaint.setStrokeWidth(dividerH);
        divPaint.setStyle(Paint.Style.STROKE);

        // Top divider
        float div1Y = startY + dividerH / 2f;
        canvas.drawLine(blockCX - dividerW / 2f, div1Y, blockCX + dividerW / 2f, div1Y, divPaint);

        // Title
        float titleTop = div1Y + gap;
        canvas.save();
        canvas.translate(blockL, titleTop);
        titleLayout.draw(canvas);
        canvas.restore();

        // Bottom divider
        float div2Y = titleTop + titleLayout.getHeight() + gap;
        canvas.drawLine(blockCX - dividerW / 2f, div2Y, blockCX + dividerW / 2f, div2Y, divPaint);

        // Author
        float authorY = div2Y + gap + marginExtra + authorSz;
        canvas.drawText(upperAuthor, blockCX - authorTextW / 2f, authorY, authorPaint);
    }
}
