package com.example.bookworm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.data.UserSession;
import com.example.bookworm.ui.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        TextView tabRegister = findViewById(R.id.tab_register);
        Button btnLogin = findViewById(R.id.btn_login);

        tabRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        btnLogin.setOnClickListener(v -> validateAndLogin());
    }

    private void validateAndLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            showError("Username must be filled.");
            return;
        }
        if (password.isEmpty()) {
            showError("Password must be filled.");
            return;
        }
        if (!password.matches("[a-zA-Z0-9]+")) {
            showError("Password must be alphanumeric.");
            return;
        }

        UserSession.username = username;
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void showError(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Invalid Input")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
