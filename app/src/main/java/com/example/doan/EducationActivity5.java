package com.example.doan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class EducationActivity5 extends AppCompatActivity {

    TextView bubbleTextView;
    EditText answerInput;
    Button checkButton;

    SQLiteDatabase database;

    String correctAnswer = "";
    boolean isCheckMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education5);

        // Ánh xạ view
        bubbleTextView = findViewById(R.id.bubbleTextView);
        answerInput = findViewById(R.id.editTextAnswer);
        checkButton = findViewById(R.id.buttonCheck);

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(EducationActivity5.this, EducationActivity4.class));
            finish();
        });

        // Load ảnh GIF động
        ImageView imageView = findViewById(R.id.img);
        Glide.with(this)
                .asGif()
                .load(R.drawable.edu)
                .into(imageView);

        // Nút kiểm tra / tiếp tục
        checkButton.setOnClickListener(v -> {
            String userInput = answerInput.getText().toString().trim().toLowerCase();

            if (isCheckMode) {
                if (userInput.equals(correctAnswer)) {
                    Toast.makeText(this, "🎉 Chính xác!", Toast.LENGTH_SHORT).show();
                    checkButton.setText("TIẾP TỤC");
                    checkButton.setBackgroundColor(Color.parseColor("#4CAF50"));
                    checkButton.setTextColor(Color.WHITE);
                    isCheckMode = false;
                } else {
                    Toast.makeText(this, "❌ Sai rồi! Thử lại...", Toast.LENGTH_SHORT).show();
                }
            } else {
                // ➤ Chuyển sang Activity khác sau khi đúng và bấm "TIẾP TỤC"
                Intent intent = new Intent(EducationActivity5.this, EducationActivity6.class);
                startActivity(intent);
                finish();
            }
        });

        // Tải dữ liệu đầu tiên
        loadRandomTranslation();
    }

    private void loadRandomTranslation() {
        try {
            File dbFile = getDatabasePath("english_list.db");

            if (!dbFile.exists()) {
                Toast.makeText(this, "Không tìm thấy CSDL!", Toast.LENGTH_SHORT).show();
                return;
            }

            database = SQLiteDatabase.openDatabase(
                    dbFile.getPath(),
                    null,
                    SQLiteDatabase.OPEN_READONLY
            );

            Cursor cursor = database.rawQuery("SELECT USA, VN FROM list ORDER BY RANDOM() LIMIT 1", null);

            if (cursor.moveToFirst()) {
                String english = cursor.getString(0);  // VD: "staff room"
                String vietnamese = cursor.getString(1); // VD: "Phòng nghỉ giáo viên"

                correctAnswer = english.trim().toLowerCase(); // So sánh không phân biệt hoa thường
                bubbleTextView.setText(vietnamese.trim() + "?");
                answerInput.setHint("Nhập bản dịch tiếng Anh");
            }

            cursor.close();
            database.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Lỗi CSDL: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
