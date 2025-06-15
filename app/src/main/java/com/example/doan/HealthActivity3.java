package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;

public class HealthActivity3 extends Activity {

    private LinearLayout boxShortness, boxChestpain, boxDifficult, boxLoss, boxDehydration, boxAllergies;
    private TextView wordText;
    private Button checkButton;
    private ImageView speakerButton;
    private String correctEnglish;
    private LinearLayout selectedBox = null;
    private LinearLayout correctBox = null;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health3); // hoặc activity_health3.xml nếu bạn đặt tên khác

        // Ánh xạ
        boxShortness = findViewById(R.id.boxShortness);
        boxChestpain = findViewById(R.id.boxChestpain);
        boxDifficult = findViewById(R.id.boxDifficult);
        boxLoss = findViewById(R.id.boxLoss);
        boxDehydration = findViewById(R.id.boxDehydration);
        boxAllergies = findViewById(R.id.boxAllergies);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        // DB Helper
        DatabaseHealth3 dbHelper = new DatabaseHealth3(this);
        String[] randomWord = dbHelper.getRandomWord();
        correctEnglish = randomWord[0].trim().toLowerCase(); // ví dụ: "shortness of breath"
        wordText.setText(correctEnglish);

        // Gán tag cho từng box
        boxShortness.setTag("shortness of breath");
        boxChestpain.setTag("chest pain");
        boxDifficult.setTag("difficult to swallow");
        boxLoss.setTag("loss of appetite");
        boxDehydration.setTag("dehydration");
        boxAllergies.setTag("allergies");

        correctBox = getBoxByWord(correctEnglish);

        // Bắt sự kiện chọn
        View.OnClickListener boxClickListener = v -> {
            if (checkButton.getText().equals("Tiếp tục")) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxShortness.setOnClickListener(boxClickListener);
        boxChestpain.setOnClickListener(boxClickListener);
        boxDifficult.setOnClickListener(boxClickListener);
        boxLoss.setOnClickListener(boxClickListener);
        boxDehydration.setOnClickListener(boxClickListener);
        boxAllergies.setOnClickListener(boxClickListener);

        // Nút kiểm tra
        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (selectedBox.equals(correctBox)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                checkButton.setText("Tiếp tục");
                disableBoxes();
                checkButton.setOnClickListener(view -> {
                    Intent intent = new Intent(HealthActivity3.this, HealthActivity4.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
            }
        });

        // Quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(HealthActivity3.this, HealthActivity2.class));
            finish();
        });

        // Phát âm
        speakerButton.setOnClickListener(v -> playAudio(correctEnglish));
    }

    private LinearLayout getBoxByWord(String word) {
        switch (word) {
            case "shortness of breath": return boxShortness;
            case "chest pain": return boxChestpain;
            case "difficult to swallow": return boxDifficult;
            case "loss of appetite": return boxLoss;
            case "dehydration": return boxDehydration;
            case "allergies": return boxAllergies;
            default: return null;
        }
    }

    private void highlightSelected(LinearLayout selected) {
        boxShortness.setBackground(null);
        boxChestpain.setBackground(null);
        boxDifficult.setBackground(null);
        boxLoss.setBackground(null);
        boxDehydration.setBackground(null);
        boxAllergies.setBackground(null);

        selected.setBackgroundResource(R.drawable.language_item_bg);
        checkButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
    }

    private void disableBoxes() {
        boxShortness.setClickable(false);
        boxChestpain.setClickable(false);
        boxDifficult.setClickable(false);
        boxLoss.setClickable(false);
        boxDehydration.setClickable(false);
        boxAllergies.setClickable(false);
    }

    private void playAudio(String fileName) {
        if (mediaPlayer != null) mediaPlayer.release();
        int resId = getResources().getIdentifier(fileName.replace(" ", "_"), "raw", getPackageName());
        if (resId != 0) {
            mediaPlayer = MediaPlayer.create(this, resId);
            mediaPlayer.start();
        } else {
            Toast.makeText(this, "Không tìm thấy file âm thanh", Toast.LENGTH_SHORT).show();
        }
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
