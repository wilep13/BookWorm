package com.example.bookworm.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.bookworm.R;
import com.example.bookworm.ui.auth.LoginActivity;
import com.example.bookworm.ui.home.BooksFragment;
import com.example.bookworm.ui.home.HomeFragment;
import com.example.bookworm.ui.home.StoresFragment;
import com.example.bookworm.view.NavbarView;

public class MainActivity extends AppCompatActivity {

    private NavbarView navbar;
    private final Fragment[] fragments = new Fragment[3];
    private int currentSlot = NavbarView.SLOT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navbar = findViewById(R.id.navbar);

        if (savedInstanceState == null) {
            fragments[0] = new HomeFragment();
            fragments[1] = new BooksFragment();
            fragments[2] = new StoresFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragments[0], "HOME")
                    .add(R.id.fragment_container, fragments[1], "BOOKS").hide(fragments[1])
                    .add(R.id.fragment_container, fragments[2], "STORES").hide(fragments[2])
                    .commitNow();

            int requestedTab = getIntent().getIntExtra("tab", NavbarView.SLOT_HOME);
            if (requestedTab != NavbarView.SLOT_HOME) {
                currentSlot = requestedTab;
                getSupportFragmentManager().beginTransaction()
                        .show(fragments[requestedTab]).hide(fragments[NavbarView.SLOT_HOME]).commitNow();
            }
        } else {
            currentSlot = savedInstanceState.getInt("currentSlot", NavbarView.SLOT_HOME);
            FragmentManager fm = getSupportFragmentManager();
            fragments[0] = fm.findFragmentByTag("HOME");
            fragments[1] = fm.findFragmentByTag("BOOKS");
            fragments[2] = fm.findFragmentByTag("STORES");
        }

        navbar.setSlot(currentSlot);
        navbar.setOnTabClickListener(slot -> {
            if (slot == NavbarView.SLOT_LOGOUT) { logout(); return; }
            switchToTab(slot);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSlot", currentSlot);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int tab = intent.getIntExtra("tab", -1);
        if (tab >= 0 && tab < fragments.length) switchToTab(tab);
    }

    public void switchToTab(int slot) {
        if (slot == currentSlot || slot >= fragments.length) return;
        Fragment show = fragments[slot];
        Fragment hide = fragments[currentSlot];
        currentSlot = slot;
        navbar.animateToSlot(slot);
        getSupportFragmentManager().beginTransaction()
                .show(show)
                .hide(hide)
                .commit();
    }

    public void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }

    public void showAvatarMenu(View anchor) {
        View content = getLayoutInflater().inflate(R.layout.popup_avatar_menu, null);
        LinearLayout container = content.findViewById(R.id.menu_container);

        if (currentSlot != NavbarView.SLOT_HOME)
            addMenuItem(container, R.drawable.ic_nav_home_v2, "Home",
                    false, () -> switchToTab(NavbarView.SLOT_HOME));
        if (currentSlot != NavbarView.SLOT_BOOKS)
            addMenuItem(container, R.drawable.ic_nav_books_v2, "All Books",
                    false, () -> switchToTab(NavbarView.SLOT_BOOKS));
        if (currentSlot != NavbarView.SLOT_STORES)
            addMenuItem(container, R.drawable.ic_nav_cart, "Our Store",
                    false, () -> switchToTab(NavbarView.SLOT_STORES));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout, "Log Out", true, this::logout);

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
        int color = isLogout ? getColor(R.color.color_error) : getColor(R.color.color_primary_3);
        text.setText(label);
        text.setTextColor(color);
        icon.setImageResource(iconRes);
        Drawable d = icon.getDrawable();
        if (d != null) {
            d = d.mutate();
            d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            icon.setImageDrawable(d);
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
}
