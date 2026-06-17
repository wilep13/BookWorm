package com.example.bookworm.ui.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

        navHome .setOnClickListener(v -> navigateTo(HomeActivity.class));
        navBooks.setOnClickListener(v -> navigateTo(BooksActivity.class));
        navLogout.setOnClickListener(v -> logout());
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
