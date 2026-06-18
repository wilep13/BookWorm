package com.example.bookworm.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import com.example.bookworm.R;
import com.example.bookworm.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivLogo        = findViewById(R.id.iv_logo);
        View      viewShadow    = findViewById(R.id.view_shadow);
        TextView  tvTagline     = findViewById(R.id.tv_tagline);
        Button    btnGetStarted = findViewById(R.id.btn_get_started);

        // Initial state: hide logo (positioned above screen after layout),
        // shadow invisible, text & button invisible
        ivLogo.setAlpha(0f);
        viewShadow.setAlpha(0f);
        tvTagline.setAlpha(0f);
        tvTagline.setTranslationY(dpToPx(24));
        btnGetStarted.setAlpha(0f);
        btnGetStarted.setTranslationY(dpToPx(24));

        ivLogo.post(() -> {
            // Place logo fully above the screen before making it visible
            ivLogo.setTranslationY(-(ivLogo.getTop() + ivLogo.getHeight()));
            ivLogo.setAlpha(1f);

            // Stage 1 (0–300ms): shadow fades in — landing-spot hint
            viewShadow.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .withEndAction(() ->
                            // Stage 2b (550–850ms): shadow absorbed as logo arrives
                            viewShadow.animate()
                                    .alpha(0f)
                                    .setDuration(300)
                                    .setStartDelay(250)
                                    .start()
                    )
                    .start();

            // Stage 2a (150–850ms): logo drops from above with subtle bounce on landing
            ivLogo.animate()
                    .translationY(0f)
                    .setDuration(700)
                    .setStartDelay(150)
                    .setInterpolator(new OvershootInterpolator(0.6f))
                    .withEndAction(() -> {
                        // Stage 3 (930–1230ms): tagline rises and fades in
                        tvTagline.animate()
                                .alpha(1f)
                                .translationY(0f)
                                .setDuration(300)
                                .setStartDelay(80)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                        // Stage 4 (1130–1430ms): button fades in
                        btnGetStarted.animate()
                                .alpha(1f)
                                .translationY(0f)
                                .setDuration(300)
                                .setStartDelay(280)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                    })
                    .start();
        });

        btnGetStarted.setOnClickListener(v -> goToLogin());
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
