package com.example.bookworm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tabRegister = findViewById(R.id.tab_register);
        Button btnLogin = findViewById(R.id.btn_login);
        LinearLayout btnGoogle = findViewById(R.id.btn_google);

        tabRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });

        btnGoogle.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
