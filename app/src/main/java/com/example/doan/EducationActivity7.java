package com.example.doan;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class EducationActivity7 extends AppCompatActivity {
    private Button btnOption1, btnOption2, btnCheck;
    private ImageButton btnSound;
    private TextView tvAnswerWord;

    private String correctAnswer = "college";
    private String selectedAnswer = null;
    private Button selectedButton = null;

    private boolean isAnsweredCorrectly = false;
    private MediaPlayer mediaPlayer;

    // Dữ liệu từ các màn trước
    private int xp = 0;
    private int correct = 0;
    private int total = 0;
    private long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education7);

        // Nhận dữ liệu từ EducationActivity6
        xp = getIntent().getIntExtra("xp", 0);
        correct = getIntent().getIntExtra("correct", 0);
        total = getIntent().getIntExtra("total", 0);
        startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        btnOption1 = findViewById(R.id.btn_option1);
        btnOption2 = findViewById(R.id.btn_option2);
        btnCheck = findViewById(R.id.btn_check);
        btnSound = findViewById(R.id.sound_icon);
        tvAnswerWord = findViewById(R.id.tv_answer_word);

        tvAnswerWord.setVisibility(View.INVISIBLE);
        btnCheck.setEnabled(false);
        btnCheck.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        // Phát âm
        btnSound.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, R.raw.college);
            mediaPlayer.start();
        });

        // Sự kiện chọn đáp án
        View.OnClickListener optionClick = v -> {
            if (isAnsweredCorrectly) return;

            Button clickedButton = (Button) v;

            if (selectedButton != null) {
                selectedButton.setBackgroundResource(R.drawable.language_item_bg);
            }

            selectedButton = clickedButton;
            selectedAnswer = clickedButton.getText().toString().toLowerCase();
            clickedButton.setBackgroundResource(R.drawable.language_item_bg);

            btnCheck.setEnabled(true);
            btnCheck.setText("KIỂM TRA");
            btnCheck.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
        };

        btnOption1.setOnClickListener(optionClick);
        btnOption2.setOnClickListener(optionClick);

        // Kiểm tra kết quả
        btnCheck.setOnClickListener(v -> {
            if (selectedAnswer == null) {
                Toast.makeText(this, "Vui lòng chọn một đáp án", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isAnsweredCorrectly) {
                total++; // mỗi lần kiểm tra là thêm 1 câu

                if (selectedAnswer.equals(correctAnswer)) {
                    Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();
                    isAnsweredCorrectly = true;
                    correct++;
                    xp += 10;

                    btnCheck.setText("TIẾP TỤC");
                    tvAnswerWord.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "❌ Sai rồi!", Toast.LENGTH_SHORT).show();

                    if (selectedButton != null) {
                        selectedButton.setBackgroundResource(R.drawable.language_option_default);
                    }

                    selectedButton = null;
                    selectedAnswer = null;

                    btnCheck.setEnabled(false);
                    btnCheck.setText("KIỂM TRA");
                    btnCheck.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
                }
            } else {
                // Khi đúng và bấm TIẾP TỤC → sang màn kết quả
                Intent intent = new Intent(EducationActivity7.this, EducationActivityEnd.class);
                intent.putExtra("xp", xp);
                intent.putExtra("correct", correct);
                intent.putExtra("total", total);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
                finish();
            }
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
