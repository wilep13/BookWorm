package com.example.bookworm.ui.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.adapter.StoreAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.ui.BaseActivity;

public class StoresActivity extends BaseActivity {

    private NestedScrollView nsvStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        nsvStores = findViewById(R.id.nsv_stores);
        setupStoreList();
        setupNavbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nsvStores != null) nsvStores.scrollTo(0, 0);
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

    @Override
    protected void populateAvatarMenu(LinearLayout container) {
        addMenuItem(container, R.drawable.ic_nav_home_v2,  "Home",      false, () -> navigateTo(HomeActivity.class));
        addMenuItem(container, R.drawable.ic_nav_books_v2, "All Books", false, () -> navigateTo(BooksActivity.class));
        addSeparator(container);
        addMenuItem(container, R.drawable.ic_nav_logout,   "Log Out",   true,  this::logout);
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
