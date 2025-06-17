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

public class FamilyActivity6 extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    Button btnSpeak, btnCheck;
    TextView textStatus, textSentence;
    String expectedSentence = "Father.";
    private MediaPlayer mediaPlayer;

    boolean isCheckMode = true;

    // Biến thống kê
    int xp = 0;
    int correct = 0;
    int total = 0;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family6);

        // Nhận dữ liệu từ FamilyActivity5
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
        ImageView soundIcon = findViewById(R.id.sound_icon);

        textSentence.setText(expectedSentence);
        mediaPlayer = MediaPlayer.create(this, R.raw.father);
        soundIcon.setOnClickListener(v -> mediaPlayer.start());

        btnSpeak.setOnClickListener(view -> startSpeechRecognition());

        btnCheck.setOnClickListener(view -> {
            if (isCheckMode) {
                String spokenText = textStatus.getText().toString().trim().toLowerCase().replaceAll("[^a-z]", "");
                String expected = expectedSentence.trim().toLowerCase().replaceAll("[^a-z]", "");

                if (spokenText.equals(expected)) {
                    Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();
                    btnCheck.setText("TIẾP TỤC");
                    btnCheck.setBackgroundTintList(getColorStateList(R.color.green));
                    isCheckMode = false;

                    // Cập nhật thống kê
                    xp += 15;
                    correct += 1;
                    total += 1;

                } else {
                    Toast.makeText(this, "❌ Sai rồi, hãy thử lại", Toast.LENGTH_SHORT).show();
                }

            } else {
                // ➤ Chuyển sang màn kết quả (FamilyActivity7)
                Intent resultIntent = new Intent(FamilyActivity6.this, FamilyActivityEnd.class);
                resultIntent.putExtra("xp", xp);
                resultIntent.putExtra("correct", correct);
                resultIntent.putExtra("total", total);
                resultIntent.putExtra("startTime", startTime);
                startActivity(resultIntent);
                finish();
            }
        });

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(FamilyActivity6.this, FamilyActivity5.class);
            backIntent.putExtra("xp", xp);
            backIntent.putExtra("correct", correct);
            backIntent.putExtra("total", total);
            backIntent.putExtra("startTime", startTime);
            startActivity(backIntent);
            finish();
        });

        // Load ảnh động gif
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
            Toast.makeText(this, "Không thể khởi động nhận diện giọng nói", Toast.LENGTH_SHORT).show();
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
