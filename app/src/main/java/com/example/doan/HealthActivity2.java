package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.*;

import com.bumptech.glide.Glide;

import java.util.*;

public class HealthActivity2 extends Activity {

    private GridLayout wordOptionsLayout;
    private Button checkButton;
    private StringBuilder userAnswer = new StringBuilder();
    private final String correctAnswer = "Mệt mỏi";
    private MediaPlayer mediaPlayer;
    private TextView englishText;
    private boolean answeredCorrectly = false;

    private final String[] wordSuggestions = {
            "Ho", "Đau cơ", "Mệt mỏi", "Mẫn đỏ", "Sổ mũi", "Sốt"
    };

    private final List<Button> wordButtons = new ArrayList<>();

    // Thống kê
    private int xp = 0;
    private int correct = 0;
    private int total = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health2);

        // Nhận dữ liệu thống kê từ HealthActivity1
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        wordOptionsLayout = findViewById(R.id.word_options);
        checkButton = findViewById(R.id.check_button);
        ImageView soundIcon = findViewById(R.id.sound_icon);
        englishText = findViewById(R.id.english_text);
        ImageView imageView = findViewById(R.id.character_image);
        ImageView backArrow = findViewById(R.id.back_arrow);

        mediaPlayer = MediaPlayer.create(this, R.raw.fatigue);

        checkButton.setEnabled(false);
        checkButton.setBackgroundColor(Color.parseColor("#BDBDBD")); // xám

        soundIcon.setOnClickListener(v -> mediaPlayer.start());

        // Load ảnh gif
        Glide.with(this).asGif().load(R.drawable.edu).into(imageView);

        // Tạo các từ gợi ý
        for (String word : wordSuggestions) {
            Button wordButton = new Button(this);
            wordButton.setText(word);
            wordButton.setBackgroundResource(android.R.drawable.btn_default_small);

            wordButton.setOnClickListener(view -> {
                userAnswer.append(word).append(" ");
                checkButton.setEnabled(true);
                checkButton.setBackgroundColor(Color.parseColor("#4CAF50")); // xanh lá khi chọn
                wordButton.setEnabled(false);
            });

            wordButtons.add(wordButton);
            wordOptionsLayout.addView(wordButton);
        }

        checkButton.setOnClickListener(v -> {
            if (!answeredCorrectly) {
                String answer = userAnswer.toString().trim();

                if (answer.contains(correctAnswer)) {
                    Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                    answeredCorrectly = true;

                    // Cập nhật thống kê
                    xp += 15;
                    correct += 1;
                    total += 1;

                    checkButton.setText("TIẾP TỤC");
                    checkButton.setBackgroundColor(Color.parseColor("#4CAF50"));
                } else {
                    Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
                    userAnswer.setLength(0);
                    for (Button b : wordButtons) b.setEnabled(true);
                    checkButton.setEnabled(false);
                    checkButton.setBackgroundColor(Color.parseColor("#BDBDBD"));
                }
            } else {
                // Chuyển tiếp và gửi dữ liệu
                Intent nextIntent = new Intent(HealthActivity2.this, HealthActivity3.class);
                nextIntent.putExtra("xp", xp);
                nextIntent.putExtra("correct", correct);
                nextIntent.putExtra("total", total);
                nextIntent.putExtra("startTime", startTime);
                startActivity(nextIntent);
                finish();
            }
        });

        // Nút quay lại
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(HealthActivity2.this, HealthActivity1.class);
            backIntent.putExtra("xp", xp);
            backIntent.putExtra("correct", correct);
            backIntent.putExtra("total", total);
            backIntent.putExtra("startTime", startTime);
            startActivity(backIntent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
