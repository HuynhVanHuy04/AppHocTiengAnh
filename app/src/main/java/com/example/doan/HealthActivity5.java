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

public class HealthActivity5 extends AppCompatActivity {

    TextView bubbleTextView;
    EditText answerInput;
    Button checkButton;

    SQLiteDatabase database;
    String correctAnswer = "";
    boolean isCheckMode = true;

    // Thống kê
    int xp, correct, total;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health5);

        // Nhận dữ liệu thống kê từ HealthActivity4
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        // Ánh xạ view
        bubbleTextView = findViewById(R.id.bubbleTextView);
        answerInput = findViewById(R.id.editTextAnswer);
        checkButton = findViewById(R.id.buttonCheck);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(HealthActivity5.this, HealthActivity4.class);
            backIntent.putExtra("xp", xp);
            backIntent.putExtra("correct", correct);
            backIntent.putExtra("total", total);
            backIntent.putExtra("startTime", startTime);
            startActivity(backIntent);
            finish();
        });

        // Load ảnh GIF động
        ImageView imageView = findViewById(R.id.img);
        Glide.with(this).asGif().load(R.drawable.edu).into(imageView);

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
                // ➤ Tăng điểm nếu đúng
                xp += 10;
                correct++;
                total++;

                Intent nextIntent = new Intent(HealthActivity5.this, HealthActivity6.class);
                nextIntent.putExtra("xp", xp);
                nextIntent.putExtra("correct", correct);
                nextIntent.putExtra("total", total);
                nextIntent.putExtra("startTime", startTime);
                startActivity(nextIntent);
                finish();
            }
        });

        // Tải câu hỏi ngẫu nhiên
        loadRandomTranslation();
    }

    private void loadRandomTranslation() {
        try {
            File dbFile = getDatabasePath("health.db");

            if (!dbFile.exists()) {
                Toast.makeText(this, "Không tìm thấy CSDL!", Toast.LENGTH_SHORT).show();
                return;
            }

            database = SQLiteDatabase.openDatabase(
                    dbFile.getPath(),
                    null,
                    SQLiteDatabase.OPEN_READONLY
            );

            Cursor cursor = database.rawQuery("SELECT USA, VN FROM Health_list ORDER BY RANDOM() LIMIT 1", null);

            if (cursor.moveToFirst()) {
                String english = cursor.getString(0);
                String vietnamese = cursor.getString(1);

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
