package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.*;

import com.bumptech.glide.Glide;

import java.util.*;

public class FamilyActivity2 extends Activity {

    private GridLayout wordOptionsLayout;
    private Button checkButton;
    private StringBuilder userAnswer = new StringBuilder();
    private final String correctAnswer = "Ba mẹ";
    private MediaPlayer mediaPlayer;
    private TextView englishText;
    private boolean answeredCorrectly = false;

    private final String[] wordSuggestions = {
            "Mẹ", "Bố", "Anh trai", "Con cái", "Ba mẹ", "Cháu trai"
    };

    private final List<Button> wordButtons = new ArrayList<>();

    // Dữ liệu nhận từ màn trước
    private int xp = 0;
    private int correct = 0;
    private int total = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family2);

        wordOptionsLayout = findViewById(R.id.word_options);
        checkButton = findViewById(R.id.check_button);
        ImageView soundIcon = findViewById(R.id.sound_icon);
        englishText = findViewById(R.id.english_text);

        mediaPlayer = MediaPlayer.create(this, R.raw.parent);

        // Nhận dữ liệu từ FamilyActivity1
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        // Trạng thái ban đầu
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
                checkButton.setBackgroundColor(Color.parseColor("#4CAF50")); // xanh lá
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

                    // Reset trạng thái
                    userAnswer.setLength(0);
                    for (Button b : wordButtons) b.setEnabled(true);
                    checkButton.setEnabled(false);
                    checkButton.setBackgroundColor(Color.parseColor("#BDBDBD")); // xám
                }
            } else {
                // Trả lời đúng, cập nhật dữ liệu
                xp += 11;
                correct += 1;
                total += 1;

                Intent nextIntent = new Intent(FamilyActivity2.this, FamilyActivity3.class);
                nextIntent.putExtra("xp", xp);
                nextIntent.putExtra("correct", correct);
                nextIntent.putExtra("total", total);
                nextIntent.putExtra("startTime", startTime);
                startActivity(nextIntent);
                finish();
            }
        });

        ImageView imageView = findViewById(R.id.character_image);
        Glide.with(this).asGif().load(R.drawable.edu).into(imageView); // ảnh gif nhân vật

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(FamilyActivity2.this, FamilyActivity1.class));
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
