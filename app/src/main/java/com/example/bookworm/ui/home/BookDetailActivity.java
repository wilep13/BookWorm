package com.example.bookworm.ui.home;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;
    private int qty = 1;
    private TextView tvQty, tvTotal, tvPriceEach;
    private EditText etAddress, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        String bookId = getIntent().getStringExtra("book_id");
        book = Catalogue.findById(bookId != null ? bookId : "");
        if (book == null) { finish(); return; }

        etAddress = findViewById(R.id.et_address);
        etPhone   = findViewById(R.id.et_phone);

        bindBook();
        setupQtyControls();
        setupButtons();
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
            tvDropcap.post(() -> {
                int gap = Math.round(5 * getResources().getDisplayMetrics().density);
                int dropCapW = tvDropcap.getWidth() + gap;
                SpannableString ss = new SpannableString(rest);
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
        btnBuy.setOnClickListener(v -> validateAndOrder());
    }

    private void validateAndOrder() {
        String address = etAddress.getText().toString().trim();
        String phone   = etPhone.getText().toString().trim();

        if (address.isEmpty() || phone.isEmpty()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Incomplete Form")
                    .setMessage("Address and phone number must be filled.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        if (!TextUtils.isDigitsOnly(phone)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Invalid Phone Number")
                    .setMessage("Phone number must be numeric.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        View customView = getLayoutInflater().inflate(R.layout.dialog_order_success, null);
        ((TextView) customView.findViewById(R.id.tv_success_body)).setText(
                "A confirmation email has been sent to your email address. "
                + book.getTitle() + " is on its way!");

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(customView)
                .setCancelable(false)
                .create();

        customView.findViewById(R.id.btn_back_to_books).setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, com.example.bookworm.ui.MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("tab", com.example.bookworm.view.NavbarView.SLOT_BOOKS);
            startActivity(intent);
            finish();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
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
