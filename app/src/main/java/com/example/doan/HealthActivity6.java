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

public class HealthActivity6 extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    Button btnSpeak, btnCheck;
    TextView textStatus, textSentence;
    String expectedSentence = "Toothache."; // Câu mẫu
    private MediaPlayer mediaPlayer;

    // Thống kê
    int xp, correct, total;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health6);

        // Nhận dữ liệu từ HealthActivity5
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        // Ánh xạ View
        btnSpeak = findViewById(R.id.btnSpeak);
        btnCheck = findViewById(R.id.btnCheck);
        textStatus = findViewById(R.id.textStatus);
        textSentence = findViewById(R.id.textSentence);

        textSentence.setText(expectedSentence);

        ImageView soundIcon = findViewById(R.id.sound_icon);
        mediaPlayer = MediaPlayer.create(this, R.raw.toothache);
        soundIcon.setOnClickListener(v -> mediaPlayer.start());

        btnSpeak.setOnClickListener(view -> startSpeechRecognition());

        btnCheck.setOnClickListener(view -> {
            String spokenText = textStatus.getText().toString().trim().toLowerCase().replaceAll("[^a-z]", "");
            String expected = expectedSentence.trim().toLowerCase().replaceAll("[^a-z]", "");

            if (spokenText.equals(expected)) {
                Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();

                // ➤ Cộng điểm
                xp += 10;
                correct++;
                total++;

                // ➤ Chuyển đến màn hình kết quả
                Intent resultIntent = new Intent(HealthActivity6.this, HealthActivityEnd.class);
                resultIntent.putExtra("xp", xp);
                resultIntent.putExtra("correct", correct);
                resultIntent.putExtra("total", total);
                resultIntent.putExtra("startTime", startTime);
                startActivity(resultIntent);
                finish();
            } else {
                Toast.makeText(this, "❌ Sai rồi, hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(HealthActivity6.this, HealthActivity5.class);
            backIntent.putExtra("xp", xp);
            backIntent.putExtra("correct", correct);
            backIntent.putExtra("total", total);
            backIntent.putExtra("startTime", startTime);
            startActivity(backIntent);
            finish();
        });

        ImageView imageView = findViewById(R.id.imageCharacter);
        Glide.with(this)
                .asGif()
                .load(R.drawable.edu)
                .into(imageView);
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở nhận diện giọng nói", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                textStatus.setText(spokenText);
                btnCheck.setEnabled(true);
                btnCheck.setBackgroundTintList(getColorStateList(R.color.green));
            }
        }
    }
}
