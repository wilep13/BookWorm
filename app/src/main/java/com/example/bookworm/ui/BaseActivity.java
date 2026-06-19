package com.example.bookworm.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.ui.auth.LoginActivity;

public abstract class BaseActivity extends AppCompatActivity {

    // ── Navigation helpers ───────────────────────────────────────────────────

    protected void navigateTo(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // ── Avatar popup ─────────────────────────────────────────────────────────

    protected void showAvatarMenu(View anchor) {
        View content = getLayoutInflater().inflate(R.layout.popup_avatar_menu, null);
        LinearLayout container = content.findViewById(R.id.menu_container);
        populateAvatarMenu(container);

        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popW = content.getMeasuredWidth();

        PopupWindow popup = new PopupWindow(content,
                popW,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popup.setElevation(dp(6));
        popup.setOutsideTouchable(true);
        popup.showAsDropDown(anchor, anchor.getWidth() - popW, 0);

        content.setPivotX(popW);
        content.setPivotY(0f);
        content.setScaleX(0.85f);
        content.setScaleY(0.85f);
        content.setAlpha(0f);
        content.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(160).setInterpolator(new OvershootInterpolator(1.4f)).start();
    }

    protected abstract void populateAvatarMenu(LinearLayout container);

    protected void addMenuItem(LinearLayout parent, int iconRes, String label,
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

    protected void addSeparator(LinearLayout parent) {
        View sep = new View(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1));
        p.setMargins(dp(6), dp(4), dp(6), dp(4));
        sep.setLayoutParams(p);
        sep.setBackgroundColor(getColor(R.color.color_separator));
        parent.addView(sep);
    }

    // ── Utilities ────────────────────────────────────────────────────────────

    protected int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }

    // ── Shared inner class ───────────────────────────────────────────────────

    public static class BookGridDecoration extends RecyclerView.ItemDecoration {
        private final int colGap, rowGap;

        public BookGridDecoration(int colGap, int rowGap) {
            this.colGap = colGap;
            this.rowGap = rowGap;
        }

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
