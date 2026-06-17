package com.example.bookworm.ui.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.adapter.StoreAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.ui.auth.LoginActivity;

public class StoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        setupStoreList();
        setupNavbar();
    }

    private void setupStoreList() {
        RecyclerView rv = findViewById(R.id.rv_stores);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new StoreAdapter(Catalogue.STORES));
        rv.addItemDecoration(new StoreItemDecoration(dp(16)));
        rv.setNestedScrollingEnabled(false);
    }

    private void setupNavbar() {
        ImageView navHome   = findViewById(R.id.nav_home);
        ImageView navBooks  = findViewById(R.id.nav_books);
        ImageView navLogout = findViewById(R.id.nav_logout);
        ImageView avatar    = findViewById(R.id.iv_avatar);

        navHome .setOnClickListener(v -> navigateTo(HomeActivity.class));
        navBooks.setOnClickListener(v -> navigateTo(BooksActivity.class));
        navLogout.setOnClickListener(v -> logout());
        if (avatar != null) avatar.setOnClickListener(this::showAvatarMenu);
    }

    private void showAvatarMenu(View anchor) {
        View content = getLayoutInflater().inflate(R.layout.popup_avatar_menu, null);
        LinearLayout container = content.findViewById(R.id.menu_container);

        addMenuItem(container, R.drawable.ic_nav_home_v2,  "Home",      false, () -> navigateTo(HomeActivity.class));
        addMenuItem(container, R.drawable.ic_nav_books_v2, "All Books", false, () -> navigateTo(BooksActivity.class));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout,   "Log Out",   true,  this::logout);

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
        sep.setBackgroundColor(0x14553522);
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

    private static class StoreItemDecoration extends RecyclerView.ItemDecoration {
        private final int gap;
        StoreItemDecoration(int gap) { this.gap = gap; }

        @Override
        public void getItemOffsets(@NonNull Rect out, @NonNull View v,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State s) {
            if (parent.getChildAdapterPosition(v) > 0) out.top = gap;
        }
    }
}
