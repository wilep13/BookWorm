package com.example.bookworm.ui.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bookworm.R;
import com.example.bookworm.adapter.BookAdapter;
import com.example.bookworm.adapter.CarouselAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.example.bookworm.ui.auth.LoginActivity;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 vpCarousel;
    private LinearLayout llDots;
    private final int dotCount = Catalogue.STORES.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupCarousel();
        setupBookGrid();
        setupNavbar();
    }

    private void setupCarousel() {
        vpCarousel = findViewById(R.id.vp_carousel);
        llDots = findViewById(R.id.ll_dots);

        CarouselAdapter adapter = new CarouselAdapter(Catalogue.STORES);
        adapter.setOnSlideClickListener(() -> navigateTo(StoresActivity.class));
        vpCarousel.setAdapter(adapter);

        buildDots(0);
        vpCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                buildDots(position);
            }
        });

        ImageView btnPrev = findViewById(R.id.btn_carousel_prev);
        ImageView btnNext = findViewById(R.id.btn_carousel_next);
        btnPrev.setOnClickListener(v -> {
            int cur = vpCarousel.getCurrentItem();
            vpCarousel.setCurrentItem(cur > 0 ? cur - 1 : dotCount - 1, true);
        });
        btnNext.setOnClickListener(v -> {
            int cur = vpCarousel.getCurrentItem();
            vpCarousel.setCurrentItem((cur + 1) % dotCount, true);
        });
    }

    private void buildDots(int activeIndex) {
        llDots.removeAllViews();
        int dpActive = dp(20), dpInactive = dp(8), height = dp(8), gap = dp(7);
        for (int i = 0; i < dotCount; i++) {
            View dot = new View(this);
            boolean active = i == activeIndex;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    active ? dpActive : dpInactive, height);
            if (i > 0) lp.leftMargin = gap;
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(active
                    ? R.drawable.bg_dot_active
                    : R.drawable.bg_dot_inactive);
            llDots.addView(dot);
        }
    }

    private void setupBookGrid() {
        List<Book> featured = Catalogue.getFeatured();
        RecyclerView rv = findViewById(R.id.rv_books);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        BookAdapter adapter = new BookAdapter(featured, false);
        adapter.setOnBookClickListener(book -> openBookDetail(book.getId()));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new BookGridDecoration(dp(16), dp(32)));
    }

    private void setupNavbar() {
        ImageView navBooks  = findViewById(R.id.nav_books);
        ImageView navStores = findViewById(R.id.nav_stores);
        ImageView navLogout = findViewById(R.id.nav_logout);
        ImageView avatar    = findViewById(R.id.iv_avatar);

        navBooks .setOnClickListener(v -> navigateTo(BooksActivity.class));
        navStores.setOnClickListener(v -> navigateTo(StoresActivity.class));
        navLogout.setOnClickListener(v -> logout());
        if (avatar != null) avatar.setOnClickListener(v -> navigateTo(BooksActivity.class));

        TextView tvSeeAll = findViewById(R.id.tv_see_all);
        if (tvSeeAll != null) tvSeeAll.setOnClickListener(v -> navigateTo(BooksActivity.class));
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void openBookDetail(String bookId) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("book_id", bookId);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private static class BookGridDecoration extends RecyclerView.ItemDecoration {
        private final int colGap, rowGap;
        BookGridDecoration(int colGap, int rowGap) { this.colGap = colGap; this.rowGap = rowGap; }

        @Override
        public void getItemOffsets(@NonNull Rect out, @NonNull View v,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State s) {
            int pos = parent.getChildAdapterPosition(v);
            int col = pos % 2;
            out.left  = col == 0 ? 0 : colGap / 2;
            out.right = col == 0 ? colGap / 2 : 0;
            if (pos >= 2) out.top = rowGap;
        }
    }
}
