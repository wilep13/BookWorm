package com.example.bookworm.ui.home;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.example.bookworm.R;
import com.example.bookworm.adapter.BookAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.example.bookworm.ui.auth.LoginActivity;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private BookAdapter bookAdapter;
    private List<Book> currentList = new ArrayList<>();
    private String activeTab = "Non-Fiction";
    private View tabIndicator;
    private TextView tabNonFiction, tabFiction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        setupTabs();
        setupBookGrid();
        setupNavbar();
        applyTab("Non-Fiction", false);
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

        // Set indicator width to half the container (post-layout so view has measured width)
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

        // Filter books
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

    private void showAvatarMenu(View anchor) {
        View content = getLayoutInflater().inflate(R.layout.popup_avatar_menu, null);
        LinearLayout container = content.findViewById(R.id.menu_container);

        addMenuItem(container, R.drawable.ic_nav_home_v2, "Home",      false, () -> navigateTo(HomeActivity.class));
        addMenuItem(container, R.drawable.ic_nav_store,   "Our Store", false, () -> navigateTo(StoresActivity.class));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout,  "Log Out",   true,  this::logout);

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

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
