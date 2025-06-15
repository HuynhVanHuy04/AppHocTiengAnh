package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText fullNameEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    CheckBox terms;
    Button registerButton;
    ImageButton btnFacebook, btnGoogle;
    TextView loginText;

    DatabaseRegister dbHelper; // Thêm dòng này nếu chưa có

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.fullName);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        terms = findViewById(R.id.terms);
        registerButton = findViewById(R.id.registerButton);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);
        loginText = findViewById(R.id.loginText);

        dbHelper = new DatabaseRegister(this); // khởi tạo

        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (fullName.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!terms.isChecked()) {
                Toast.makeText(this, "Vui lòng đồng ý điều khoản", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.registerUser(fullName, phone, password)) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                // Ví dụ chuyển sang màn hình intro
                Intent intent = new Intent(RegisterActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Số điện thoại đã được đăng ký", Toast.LENGTH_SHORT).show();
            }
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnFacebook.setOnClickListener(v -> {
            // Xử lý Facebook
        });

        btnGoogle.setOnClickListener(v -> {
            // Xử lý Google
        });
    }
}
