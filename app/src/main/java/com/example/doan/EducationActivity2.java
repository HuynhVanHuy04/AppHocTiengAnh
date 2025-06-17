package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.*;

import com.bumptech.glide.Glide;

import java.util.*;

public class EducationActivity2 extends Activity {

    private GridLayout wordOptionsLayout;
    private Button checkButton;
    private StringBuilder userAnswer = new StringBuilder();
    private final String correctAnswer = "xin chào";
    private MediaPlayer mediaPlayer;
    private TextView englishText;
    private boolean answeredCorrectly = false;

    private final String[] wordSuggestions = {
            "Mời", "trà", "Vui", "vào", "xin chào", "đường"
    };

    private final List<Button> wordButtons = new ArrayList<>();
    private int xp, correct, total;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education2);
        xp = getIntent().getIntExtra("xp", 0);
        correct = getIntent().getIntExtra("correct", 0);
        total = getIntent().getIntExtra("total", 0);
        startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        wordOptionsLayout = findViewById(R.id.word_options);
        checkButton = findViewById(R.id.check_button);
        ImageView soundIcon = findViewById(R.id.sound_icon);
        englishText = findViewById(R.id.english_text);

        mediaPlayer = MediaPlayer.create(this, R.raw.welcome);

        // Màu và trạng thái ban đầu của nút kiểm tra
        checkButton.setEnabled(false);
        checkButton.setBackgroundColor(Color.parseColor("#BDBDBD")); // xám

        soundIcon.setOnClickListener(v -> mediaPlayer.start());

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
                    checkButton.setText("TIẾP TỤC");
                    checkButton.setBackgroundColor(Color.parseColor("#4CAF50")); // xanh lá
                } else {
                    Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
                    userAnswer.setLength(0);
                    for (Button b : wordButtons) b.setEnabled(true);
                    checkButton.setEnabled(false);
                    checkButton.setBackgroundColor(Color.parseColor("#BDBDBD"));
                }
            } else {
                // ✅ Cập nhật kết quả
                if (userAnswer.toString().trim().contains(correctAnswer)) {
                    xp += 11;
                    correct++;
                }
                total++;

                // ✅ Truyền tiếp dữ liệu
                Intent intent = new Intent(EducationActivity2.this, EducationActivity3.class);
                intent.putExtra("xp", xp);
                intent.putExtra("correct", correct);
                intent.putExtra("total", total);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
                finish();
            }
        });


        ImageView imageView = findViewById(R.id.character_image);
        Glide.with(this)
                .asGif()
                .load(R.drawable.edu) // tên file gif không cần đuôi .gif
                .into(imageView);

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(EducationActivity2.this, EducationActivity1.class));
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
