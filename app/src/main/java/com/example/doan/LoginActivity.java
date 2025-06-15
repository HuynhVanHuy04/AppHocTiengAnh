package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editPhone, editPassword;
    private CheckBox checkSavePassword;
    private Button btnLogin;
    private ImageButton btnFacebook, btnGoogle;
    private DatabaseRegister dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo database helper
        dbHelper = new DatabaseRegister(this);

        // Ánh xạ các view
        editPhone = findViewById(R.id.phoneEdit);
        editPassword = findViewById(R.id.password);
        checkSavePassword = findViewById(R.id.terms);
        btnLogin = findViewById(R.id.registerButton);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = editPhone.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    boolean loginSuccess = dbHelper.loginUser(phone, password);
                    if (loginSuccess) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc LoginActivity để không quay lại khi nhấn nút back

                    } else {
                        Toast.makeText(LoginActivity.this, "Số điện thoại hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Chức năng Facebook chưa được triển khai", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Chức năng Google chưa được triển khai", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
