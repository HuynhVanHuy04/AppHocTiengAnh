package com.example.doan;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.*;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EducationActivity6 extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    Button btnSpeak, btnCheck;
    TextView textStatus, textSentence;
    ImageView soundIcon, backArrow, imageView;

    String expectedSentence = "Student."; // Câu mẫu
    MediaPlayer mediaPlayer;

    // Trạng thái học tập
    int xp = 0;
    int correct = 0;
    int total = 0;
    long startTime = 0;

    boolean answeredCorrectly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education6);

        // Nhận dữ liệu từ Activity5
        xp = getIntent().getIntExtra("xp", 0);
        correct = getIntent().getIntExtra("correct", 0);
        total = getIntent().getIntExtra("total", 0);
        startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        // Ánh xạ view
        btnSpeak = findViewById(R.id.btnSpeak);
        btnCheck = findViewById(R.id.btnCheck);
        textStatus = findViewById(R.id.textStatus);
        textSentence = findViewById(R.id.textSentence);
        soundIcon = findViewById(R.id.sound_icon);
        backArrow = findViewById(R.id.back_arrow);
        imageView = findViewById(R.id.imageCharacter);

        textSentence.setText(expectedSentence);
        mediaPlayer = MediaPlayer.create(this, R.raw.student);

        // Phát âm thanh
        soundIcon.setOnClickListener(v -> mediaPlayer.start());

        // Mic
        btnSpeak.setOnClickListener(view -> startSpeechRecognition());

        // Quay lại
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(EducationActivity6.this, EducationActivity5.class);
            startActivity(intent);
            finish();
        });

        // Gif ảnh động
        Glide.with(this).asGif().load(R.drawable.edu).into(imageView);

        // Kiểm tra phát âm
        btnCheck.setOnClickListener(view -> {
            String spokenText = textStatus.getText().toString().trim().toLowerCase().replaceAll("[^a-z]", "");
            String expected = expectedSentence.trim().toLowerCase().replaceAll("[^a-z]", "");

            if (answeredCorrectly) {
                // Nếu đã đúng → chuyển tiếp
                Intent intent = new Intent(EducationActivity6.this, EducationActivity7.class);
                intent.putExtra("xp", xp);
                intent.putExtra("correct", correct);
                intent.putExtra("total", total);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
                finish();
                return;
            }

            // Kiểm tra kết quả
            total++; // Tăng tổng số câu làm
            if (spokenText.equals(expected)) {
                Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();
                xp += 15;
                correct++;
                answeredCorrectly = true;

                btnCheck.setText("Tiếp tục");
                btnCheck.setBackgroundTintList(getColorStateList(R.color.green));
            } else {
                Toast.makeText(this, "❌ Sai rồi, hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể ghi âm!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                textStatus.setText(spokenText); // Hiển thị
                btnCheck.setEnabled(true);
                btnCheck.setBackgroundTintList(getColorStateList(R.color.green));
            }
        }
        btnCheck.setText("Kiểm tra");
    }
}
