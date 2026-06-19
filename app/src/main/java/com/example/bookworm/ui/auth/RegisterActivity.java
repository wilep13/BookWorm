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

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilUsername, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilUsername        = findViewById(R.id.til_username);
        tilEmail           = findViewById(R.id.til_email);
        tilPassword        = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        etUsername         = findViewById(R.id.et_username);
        etEmail            = findViewById(R.id.et_email);
        etPassword         = findViewById(R.id.et_password);
        etConfirmPassword  = findViewById(R.id.et_confirm_password);

        etUsername.addTextChangedListener(clearErrorOn(tilUsername));
        etEmail.addTextChangedListener(clearErrorOn(tilEmail));
        etPassword.addTextChangedListener(clearErrorOn(tilPassword));
        etConfirmPassword.addTextChangedListener(clearErrorOn(tilConfirmPassword));

        TextView tabLogin = findViewById(R.id.tab_login);
        Button btnRegister = findViewById(R.id.btn_register);

        tabLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void validateAndRegister() {
        String username        = text(etUsername);
        String email           = text(etEmail);
        String password        = text(etPassword);
        String confirmPassword = text(etConfirmPassword);

        boolean hasError = false;

        if (username.isEmpty()) {
            tilUsername.setError("Username must be filled.");
            hasError = true;
        }
        if (email.isEmpty()) {
            tilEmail.setError("Email must be filled.");
            hasError = true;
        }
        if (password.isEmpty()) {
            tilPassword.setError("Password must be filled.");
            hasError = true;
        } else if (!password.matches("[a-zA-Z0-9]+")) {
            tilPassword.setError("Password must be alphanumeric.");
            hasError = true;
        }
        if (!hasError && !password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match.");
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
