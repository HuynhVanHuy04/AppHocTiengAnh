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

    // Thống kê
    private int xp = 0;
    private int correct = 0;
    private int total = 0;
    private long startTime;

    private boolean answeredCorrectly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health3);

        // Nhận dữ liệu từ HealthActivity2
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        // Ánh xạ view
        boxShortness = findViewById(R.id.boxShortness);
        boxChestpain = findViewById(R.id.boxChestpain);
        boxDifficult = findViewById(R.id.boxDifficult);
        boxLoss = findViewById(R.id.boxLoss);
        boxDehydration = findViewById(R.id.boxDehydration);
        boxAllergies = findViewById(R.id.boxAllergies);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        DatabaseHealth3 dbHelper = new DatabaseHealth3(this);
        String[] randomWord = dbHelper.getRandomWord();
        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        // Tag cho các box
        boxShortness.setTag("shortness of breath");
        boxChestpain.setTag("chest pain");
        boxDifficult.setTag("difficult to swallow");
        boxLoss.setTag("loss of appetite");
        boxDehydration.setTag("dehydration");
        boxAllergies.setTag("allergies");

        correctBox = getBoxByWord(correctEnglish);

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

        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (!answeredCorrectly && selectedBox.equals(correctBox)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                answeredCorrectly = true;
                checkButton.setText("Tiếp tục");
                disableBoxes();

                // Cập nhật thống kê
                xp += 15;
                correct++;
                total++;

            } else if (!answeredCorrectly) {
                Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
            } else {
                // Sang HealthActivity4 và gửi dữ liệu
                Intent nextIntent = new Intent(HealthActivity3.this, HealthActivity4.class);
                nextIntent.putExtra("xp", xp);
                nextIntent.putExtra("correct", correct);
                nextIntent.putExtra("total", total);
                nextIntent.putExtra("startTime", startTime);
                startActivity(nextIntent);
                finish();
            }
        });

        // Quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(HealthActivity3.this, HealthActivity2.class);
            backIntent.putExtra("xp", xp);
            backIntent.putExtra("correct", correct);
            backIntent.putExtra("total", total);
            backIntent.putExtra("startTime", startTime);
            startActivity(backIntent);
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
