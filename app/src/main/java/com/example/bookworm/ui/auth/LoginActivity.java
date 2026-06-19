package com.example.bookworm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookworm.R;
import com.example.bookworm.data.UserSession;
import com.example.bookworm.ui.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_password);
        etUsername  = findViewById(R.id.et_username);
        etPassword  = findViewById(R.id.et_password);

        etUsername.addTextChangedListener(clearErrorOn(tilUsername));
        etPassword.addTextChangedListener(clearErrorOn(tilPassword));

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
        String username = text(etUsername);
        String password = text(etPassword);

        boolean hasError = false;

        if (username.isEmpty()) {
            tilUsername.setError("Username must be filled.");
            hasError = true;
        }
        if (password.isEmpty()) {
            tilPassword.setError("Password must be filled.");
            hasError = true;
        } else if (!password.matches("[a-zA-Z0-9]+")) {
            tilPassword.setError("Password must be alphanumeric.");
            hasError = true;
        }

        if (hasError) return;

        UserSession.username = username;
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private static String text(TextInputEditText et) {
        Editable e = et.getText();
        return e != null ? e.toString().trim() : "";
    }

    private static TextWatcher clearErrorOn(TextInputLayout til) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { til.setError(null); }
        };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
