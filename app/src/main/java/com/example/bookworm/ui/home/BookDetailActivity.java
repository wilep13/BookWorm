package com.example.bookworm.ui.home;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;
    private int qty = 1;
    private TextView tvQty, tvTotal, tvPriceEach;
    private FrameLayout overlaySuccess;
    private LinearLayout dialogSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String bookId = getIntent().getStringExtra("book_id");
        book = Catalogue.findById(bookId != null ? bookId : "");
        if (book == null) { finish(); return; }

        bindBook();
        setupQtyControls();
        setupButtons();
        setupSuccessOverlay();
    }

    private void bindBook() {
        ImageView coverView = findViewById(R.id.book_cover);
        coverView.setImageResource(book.getCoverResId());

        ((TextView) findViewById(R.id.tv_tag))   .setText(book.getTag());
        ((TextView) findViewById(R.id.tv_title))  .setText(book.getTitle());
        ((TextView) findViewById(R.id.tv_author)) .setText("by " + book.getAuthor());
        ((TextView) findViewById(R.id.tv_year))   .setText(book.getYear());
        ((TextView) findViewById(R.id.tv_pages))  .setText(String.valueOf(book.getPages()));
        ((TextView) findViewById(R.id.tv_genre))  .setText(book.getCategory());
        String blurb = book.getBlurb();
        TextView tvDropcap = findViewById(R.id.tv_blurb_dropcap);
        TextView tvBlurb   = findViewById(R.id.tv_blurb);
        if (blurb != null && blurb.length() > 1) {
            tvDropcap.setText(String.valueOf(blurb.charAt(0)));
            final String rest = blurb.substring(1);
            // Post so drop cap is measured before we read its width
            tvDropcap.post(() -> {
                int gap = Math.round(5 * getResources().getDisplayMetrics().density);
                int dropCapW = tvDropcap.getWidth() + gap;
                SpannableString ss = new SpannableString(rest);
                // Indent first 2 lines; line 3+ flows full-width below the drop cap
                ss.setSpan(new DropCapMarginSpan(dropCapW, 2),
                        0, rest.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvBlurb.setText(ss);
            });
        } else {
            tvDropcap.setText("");
            tvBlurb.setText(blurb != null ? blurb : "");
        }

        tvPriceEach = findViewById(R.id.tv_price_each);
        tvTotal     = findViewById(R.id.tv_total);
        tvQty       = findViewById(R.id.tv_qty);

        tvPriceEach.setText(book.getPriceFormatted() + " each");
        updateTotal();
    }

    private void setupQtyControls() {
        ImageView btnMinus = findViewById(R.id.btn_qty_minus);
        ImageView btnPlus  = findViewById(R.id.btn_qty_plus);

        btnMinus.setOnClickListener(v -> {
            if (qty > 1) { qty--; tvQty.setText(String.valueOf(qty)); updateTotal(); }
        });
        btnPlus.setOnClickListener(v -> {
            if (qty < 9) { qty++; tvQty.setText(String.valueOf(qty)); updateTotal(); }
        });
    }

    private void setupButtons() {
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        Button btnBuy = findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(v -> showSuccessDialog());
    }

    private void setupSuccessOverlay() {
        overlaySuccess = findViewById(R.id.overlay_success);
        dialogSuccess  = findViewById(R.id.dialog_success);

        TextView tvBody = findViewById(R.id.tv_success_body);
        tvBody.setText(book.getTitle() + " is on its way. We'll deliver it to your address shortly. Thank you for reading with us!");

        Button btnBackToBooks = findViewById(R.id.btn_back_to_books);
        btnBackToBooks.setOnClickListener(v -> {
            overlaySuccess.setVisibility(View.GONE);
            Intent intent = new Intent(this, com.example.bookworm.ui.MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("tab", com.example.bookworm.view.NavbarView.SLOT_BOOKS);
            startActivity(intent);
            finish();
        });

        // Dismiss on backdrop tap
        overlaySuccess.setOnClickListener(v -> overlaySuccess.setVisibility(View.GONE));
        dialogSuccess.setOnClickListener(v -> { /* consume — don't dismiss */ });
    }

    private void showSuccessDialog() {
        overlaySuccess.setVisibility(View.VISIBLE);
        dialogSuccess.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.pop_in));
    }

    private void updateTotal() {
        String total = "Rp " + String.format("%,d", book.getPrice() * qty).replace(',', '.');
        tvTotal.setText(total);
    }

    private static final class DropCapMarginSpan implements LeadingMarginSpan.LeadingMarginSpan2 {
        private final int margin;
        private final int lineCount;

        DropCapMarginSpan(int margin, int lineCount) {
            this.margin = margin;
            this.lineCount = lineCount;
        }

        @Override public int getLeadingMargin(boolean first) { return first ? margin : 0; }
        @Override public int getLeadingMarginLineCount() { return lineCount; }
        @Override public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                int top, int baseline, int bottom, CharSequence text,
                int start, int end, boolean first, Layout layout) {}
    }
}
