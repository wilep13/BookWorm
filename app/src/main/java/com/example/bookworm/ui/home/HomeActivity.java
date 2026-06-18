package com.example.bookworm.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bookworm.R;
import com.example.bookworm.adapter.BookAdapter;
import com.example.bookworm.adapter.CarouselAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.example.bookworm.ui.BaseActivity;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private static final long AUTO_SLIDE_DELAY_MS = 3_000L;

    private NestedScrollView nsvHome;
    private ViewPager2 vpCarousel;
    private LinearLayout llDots;
    private int actualCount;

    private final Handler autoSlideHandler = new Handler(Looper.getMainLooper());
    private final Runnable autoSlideRunnable = new Runnable() {
        @Override
        public void run() {
            if (vpCarousel != null) {
                vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() + 1, true);
                autoSlideHandler.postDelayed(this, AUTO_SLIDE_DELAY_MS);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nsvHome = findViewById(R.id.nsv_home);
        setupCarousel();
        setupBookGrid();
        setupNavbar();
    }

    private void setupCarousel() {
        vpCarousel = findViewById(R.id.vp_carousel);
        llDots = findViewById(R.id.ll_dots);

        actualCount = Catalogue.STORES.size();
        CarouselAdapter adapter = new CarouselAdapter(Catalogue.STORES);
        adapter.setOnSlideClickListener(() -> navigateTo(StoresActivity.class));

        vpCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                buildDots(position % actualCount);
            }
        });

        vpCarousel.setAdapter(adapter);

        int initialPage = actualCount * (CarouselAdapter.LOOP_FACTOR / 2);
        vpCarousel.setCurrentItem(initialPage, false);
        buildDots(0);

        ImageView btnPrev = findViewById(R.id.btn_carousel_prev);
        ImageView btnNext = findViewById(R.id.btn_carousel_next);
        btnPrev.setOnClickListener(v ->
                vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() - 1, true));
        btnNext.setOnClickListener(v ->
                vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() + 1, true));
    }

    private void buildDots(int activeIndex) {
        llDots.removeAllViews();
        int dpActive = dp(20), dpInactive = dp(8), height = dp(8), gap = dp(7);
        for (int i = 0; i < actualCount; i++) {
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
        rv.addItemDecoration(new BookGridDecoration(dp(68), dp(42)));
    }

    private void setupNavbar() {
        ImageView navBooks  = findViewById(R.id.nav_books);
        ImageView navStores = findViewById(R.id.nav_stores);
        ImageView navLogout = findViewById(R.id.nav_logout);
        ImageView avatar    = findViewById(R.id.iv_avatar);

        navBooks .setOnClickListener(v -> navigateTo(BooksActivity.class));
        navStores.setOnClickListener(v -> navigateTo(StoresActivity.class));
        navLogout.setOnClickListener(v -> logout());
        if (avatar != null) avatar.setOnClickListener(this::showAvatarMenu);

        TextView tvSeeAll = findViewById(R.id.tv_see_all);
        if (tvSeeAll != null) tvSeeAll.setOnClickListener(v -> navigateTo(BooksActivity.class));
    }

    @Override
    protected void populateAvatarMenu(LinearLayout container) {
        addMenuItem(container, R.drawable.ic_nav_books_v2, "All Books", false, () -> navigateTo(BooksActivity.class));
        addMenuItem(container, R.drawable.ic_nav_cart,    "Our Store", false, () -> navigateTo(StoresActivity.class));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout,  "Log Out",   true,  this::logout);
    }

    private void openBookDetail(String bookId) {
        android.content.Intent intent = new android.content.Intent(this, BookDetailActivity.class);
        intent.putExtra("book_id", bookId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nsvHome != null) nsvHome.scrollTo(0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY_MS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
    }
}
