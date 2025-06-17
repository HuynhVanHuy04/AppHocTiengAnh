package com.example.doan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class EducationActivity5 extends AppCompatActivity {

    TextView bubbleTextView;
    EditText answerInput;
    Button checkButton;
    ImageView backArrow, imageView;

    SQLiteDatabase database;

    String correctAnswer = "";
    boolean isCheckMode = true;

    // Biến lưu trạng thái học tập
    int xp = 0;
    int correct = 0;
    int total = 0;
    long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education5);

        // Nhận dữ liệu từ màn trước
        xp = getIntent().getIntExtra("xp", 0);
        correct = getIntent().getIntExtra("correct", 0);
        total = getIntent().getIntExtra("total", 0);
        startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        // Ánh xạ view
        bubbleTextView = findViewById(R.id.bubbleTextView);
        answerInput = findViewById(R.id.editTextAnswer);
        checkButton = findViewById(R.id.buttonCheck);
        backArrow = findViewById(R.id.back_arrow);
        imageView = findViewById(R.id.img);

        // Nút quay lại
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(EducationActivity5.this, EducationActivity4.class));
            finish();
        });

        // Load ảnh động
        Glide.with(this)
                .asGif()
                .load(R.drawable.edu)
                .into(imageView);

        // Nút kiểm tra
        checkButton.setOnClickListener(v -> {
            String userInput = answerInput.getText().toString().trim().toLowerCase();

            if (isCheckMode) {
                if (userInput.equals(correctAnswer)) {
                    Toast.makeText(this, "🎉 Chính xác!", Toast.LENGTH_SHORT).show();

                    // ✅ Cập nhật điểm và số câu đúng
                    xp += 10;
                    correct++;
                    total++;

                    checkButton.setText("TIẾP TỤC");
                    checkButton.setBackgroundColor(Color.parseColor("#4CAF50"));
                    checkButton.setTextColor(Color.WHITE);
                    isCheckMode = false;
                } else {
                    Toast.makeText(this, "❌ Sai rồi! Thử lại...", Toast.LENGTH_SHORT).show();
                    total++; // ✅ Tăng tổng số câu đã làm
                }
            } else {
                // ➤ Chuyển sang Activity tiếp theo và truyền dữ liệu
                Intent intent = new Intent(EducationActivity5.this, EducationActivity6.class);
                intent.putExtra("xp", xp);
                intent.putExtra("correct", correct);
                intent.putExtra("total", total);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
                finish();
            }
        });

        // Tải dữ liệu từ SQLite
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

                correctAnswer = english.trim().toLowerCase();
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
