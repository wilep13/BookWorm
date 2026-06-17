package com.example.bookworm.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivLogo        = findViewById(R.id.iv_logo);
        TextView  tvTagline     = findViewById(R.id.tv_tagline);
        Button    btnGetStarted = findViewById(R.id.btn_get_started);

        // Initial state: logo below its resting position, text & button hidden
        ivLogo.setAlpha(0f);
        ivLogo.setTranslationY(dpToPx(260));
        tvTagline.setAlpha(0f);
        tvTagline.setTranslationY(dpToPx(28));
        btnGetStarted.setAlpha(0f);
        btnGetStarted.setTranslationY(dpToPx(28));

        // Stage 1 (0–600 ms): logo rises and fades in
        ivLogo.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator(1.6f))
                .withEndAction(() -> {
                    // Stage 2 (700–1000 ms): tagline fades in
                    tvTagline.animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(300)
                            .setStartDelay(100)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();
                    // Stage 3 (900–1200 ms): button fades in
                    btnGetStarted.animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(300)
                            .setStartDelay(300)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();
                })
                .start();

        btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
