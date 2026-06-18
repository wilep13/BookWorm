package com.example.bookworm.ui.home;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.adapter.BookAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.example.bookworm.ui.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends BaseActivity {

    private NestedScrollView nsvBooks;
    private BookAdapter bookAdapter;
    private List<Book> currentList = new ArrayList<>();
    private String activeTab = "Non-Fiction";
    private View tabIndicator;
    private TextView tabNonFiction, tabFiction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        nsvBooks = findViewById(R.id.nsv_books);
        setupTabs();
        setupBookGrid();
        setupNavbar();
        applyTab("Non-Fiction", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nsvBooks != null) nsvBooks.scrollTo(0, 0);
    }

    private void setupTabs() {
        tabNonFiction = findViewById(R.id.tab_non_fiction);
        tabFiction    = findViewById(R.id.tab_fiction);
        tabIndicator  = findViewById(R.id.tab_indicator);

        tabNonFiction.setOnClickListener(v -> applyTab("Non-Fiction", true));
        tabFiction   .setOnClickListener(v -> applyTab("Fiction", true));
    }

    private void applyTab(String tab, boolean animate) {
        activeTab = tab;
        tabNonFiction.setTextColor(getColor(tab.equals("Non-Fiction")
                ? android.R.color.white : R.color.color_primary_3));
        tabFiction.setTextColor(getColor(tab.equals("Fiction")
                ? android.R.color.white : R.color.color_primary_3));

        tabNonFiction.post(() -> {
            int halfW = tabNonFiction.getWidth();
            if (halfW == 0) return;
            tabIndicator.getLayoutParams().width = halfW;
            tabIndicator.requestLayout();

            float targetX = tab.equals("Non-Fiction") ? 0f : halfW;
            if (animate) {
                ObjectAnimator.ofFloat(tabIndicator, "translationX", targetX)
                        .setDuration(320)
                        .start();
            } else {
                tabIndicator.setTranslationX(targetX);
            }
        });

        currentList.clear();
        for (Book b : Catalogue.BOOKS) {
            if (b.getCategory().equals(tab)) currentList.add(b);
        }
        if (bookAdapter != null) bookAdapter.notifyDataSetChanged();
    }

    private void setupBookGrid() {
        RecyclerView rv = findViewById(R.id.rv_books);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        bookAdapter = new BookAdapter(currentList, true);
        bookAdapter.setOnBookClickListener(book -> {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rv.setAdapter(bookAdapter);
        rv.addItemDecoration(new BookGridDecoration(dp(16), dp(30)));
    }

    private void setupNavbar() {
        ImageView navHome   = findViewById(R.id.nav_home);
        ImageView navStores = findViewById(R.id.nav_stores);
        ImageView navLogout = findViewById(R.id.nav_logout);
        ImageView avatar    = findViewById(R.id.iv_avatar);

        navHome  .setOnClickListener(v -> navigateTo(HomeActivity.class));
        navStores.setOnClickListener(v -> navigateTo(StoresActivity.class));
        navLogout.setOnClickListener(v -> logout());
        if (avatar != null) avatar.setOnClickListener(this::showAvatarMenu);
    }

    @Override
    protected void populateAvatarMenu(LinearLayout container) {
        addMenuItem(container, R.drawable.ic_nav_home_v2, "Home",      false, () -> navigateTo(HomeActivity.class));
        addMenuItem(container, R.drawable.ic_nav_cart,   "Our Store", false, () -> navigateTo(StoresActivity.class));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout,  "Log Out",   true,  this::logout);
    }
}
