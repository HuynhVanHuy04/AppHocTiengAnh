package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HealthActivity6 extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    Button btnSpeak, btnCheck;
    TextView textStatus, textSentence;
    String expectedSentence = "Toothache."; // Câu mẫu người dùng cần phát âm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health6); // Giao diện layout XML

        // Ánh xạ View
        btnSpeak = findViewById(R.id.btnSpeak);
        btnCheck = findViewById(R.id.btnCheck);
        textStatus = findViewById(R.id.textStatus);
        textSentence = findViewById(R.id.textSentence);

        textSentence.setText(expectedSentence); // Hiển thị câu mẫu

        // Nút Mic
        btnSpeak.setOnClickListener(view -> startSpeechRecognition());

        // Nút Kiểm Tra
        btnCheck.setOnClickListener(view -> {
            String spokenText = textStatus.getText().toString().trim().toLowerCase().replaceAll("[^a-z]", "");
            String expected = expectedSentence.trim().toLowerCase().replaceAll("[^a-z]", "");

            if (spokenText.equals(expected)) {
                Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Sai rồi, hãy thử lại", Toast.LENGTH_SHORT).show();
            }

        });

        // Nút Quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(HealthActivity6.this, HealthActivity5.class);
            startActivity(intent);
            finish();
        });

        // Load ảnh động gif
        ImageView imageView = findViewById(R.id.imageCharacter);
        Glide.with(this)
                .asGif()
                .load(R.drawable.edu) // File gif không cần đuôi
                .into(imageView);
    }

    // Hàm bắt đầu ghi âm giọng nói tiếng Anh
    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US"); // ép tiếng Anh Mỹ
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: " + expectedSentence);

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Error starting speech recognition", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý kết quả sau khi ghi âm
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                textStatus.setText(spokenText); // Hiển thị kết quả
                btnCheck.setEnabled(true); // Mở nút kiểm tra
                btnCheck.setBackgroundTintList(getColorStateList(R.color.green));
            }
        }
    }
}
