package com.example.bookworm.ui.home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.example.bookworm.view.BookCoverView;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;
    private int qty = 1;
    private TextView tvQty, tvTotal, tvPriceEach;

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
    }

    private void bindBook() {
        BookCoverView coverView = findViewById(R.id.book_cover);
        coverView.setBook(book);

        ((TextView) findViewById(R.id.tv_tag))   .setText(book.getTag());
        ((TextView) findViewById(R.id.tv_title))  .setText(book.getTitle());
        ((TextView) findViewById(R.id.tv_author)) .setText("by " + book.getAuthor());
        ((TextView) findViewById(R.id.tv_year))   .setText(book.getYear());
        ((TextView) findViewById(R.id.tv_pages))  .setText(String.valueOf(book.getPages()));
        ((TextView) findViewById(R.id.tv_genre))  .setText(book.getCategory());
        ((TextView) findViewById(R.id.tv_blurb))  .setText(book.getBlurb());

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
        btnBuy.setOnClickListener(v ->
            Toast.makeText(this, "Order confirmed! Thank you for reading with us.", Toast.LENGTH_LONG).show()
        );
    }

    private void updateTotal() {
        String total = "Rp " + String.format("%,d", book.getPrice() * qty).replace(',', '.');
        tvTotal.setText(total);
    }
}
