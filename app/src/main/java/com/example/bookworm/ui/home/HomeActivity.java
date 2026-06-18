package com.example.bookworm.ui.home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

    private static final long AUTO_SLIDE_DELAY_MS = 3_000L;

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

        // Register callback BEFORE setAdapter so the initial onPageSelected is captured
        vpCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                buildDots(position % actualCount);
            }
        });

        vpCarousel.setAdapter(adapter);

        // Start at midpoint so the user can swipe freely in both directions
        int initialPage = actualCount * (CarouselAdapter.LOOP_FACTOR / 2);
        vpCarousel.setCurrentItem(initialPage, false);

        // Ensure first dot is active regardless of callback timing
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

    private void showAvatarMenu(View anchor) {
        View content = getLayoutInflater().inflate(R.layout.popup_avatar_menu, null);
        LinearLayout container = content.findViewById(R.id.menu_container);

        addMenuItem(container, R.drawable.ic_nav_books_v2, "All Books",  false, () -> navigateTo(BooksActivity.class));
        addMenuItem(container, R.drawable.ic_nav_cart,    "Our Store",  false, () -> navigateTo(StoresActivity.class));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout,   "Log Out",    true,  this::logout);

        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popW = content.getMeasuredWidth();

        PopupWindow popup = new PopupWindow(content,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popup.setElevation(dp(18));
        popup.setOutsideTouchable(true);
        popup.showAsDropDown(anchor, anchor.getWidth() - popW, dp(8));

        content.setPivotX(popW);
        content.setPivotY(0f);
        content.setScaleX(0.85f);
        content.setScaleY(0.85f);
        content.setAlpha(0f);
        content.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(160).setInterpolator(new OvershootInterpolator(1.4f)).start();
    }

    private void addMenuItem(LinearLayout parent, int iconRes, String label,
                             boolean isLogout, Runnable action) {
        View item = getLayoutInflater().inflate(R.layout.item_avatar_menu, parent, false);
        ImageView icon = item.findViewById(R.id.iv_menu_icon);
        TextView  text = item.findViewById(R.id.tv_menu_label);
        icon.setImageResource(iconRes);
        text.setText(label);
        if (isLogout) {
            int red = getColor(R.color.color_error);
            text.setTextColor(red);
            icon.setColorFilter(new PorterDuffColorFilter(red, PorterDuff.Mode.SRC_IN));
        }
        item.setOnClickListener(v -> action.run());
        parent.addView(item);
    }

    private void addSeparator(LinearLayout parent) {
        View sep = new View(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1));
        p.setMargins(dp(13), dp(4), dp(13), dp(4));
        sep.setLayoutParams(p);
        sep.setBackgroundColor(getColor(R.color.color_separator));
        parent.addView(sep);
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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
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
