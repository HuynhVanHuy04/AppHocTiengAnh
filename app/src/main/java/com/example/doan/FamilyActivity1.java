package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;

public class FamilyActivity1 extends Activity {

    private LinearLayout boxGr, boxGrft, boxGrmt, boxAunt, boxUncle, boxCousin;
    private ImageView imgGr, imgGrft, imgGrmt, imgAunt, imgUncle, imgCousin;
    private TextView wordText;
    private Button checkButton;
    private ImageView speakerButton;
    private String correctEnglish;
    private LinearLayout selectedBox = null;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family1);

        // Ánh xạ view
        boxGr = findViewById(R.id.boxGr);
        boxGrft = findViewById(R.id.boxGrft);
        boxGrmt = findViewById(R.id.boxGrmt);
        boxAunt = findViewById(R.id.boxAunt);
        boxUncle = findViewById(R.id.boxUncle);
        boxCousin = findViewById(R.id.boxCousin);

        imgGr = findViewById(R.id.imgGr);
        imgGrft = findViewById(R.id.imgGrft);
        imgGrmt = findViewById(R.id.imgGrmt);
        imgAunt = findViewById(R.id.imgAunt);
        imgUncle = findViewById(R.id.imgUncle);
        imgCousin = findViewById(R.id.imgCousin);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        // Gán tag để so sánh
        boxGr.setTag("grandparents");
        boxGrft.setTag("grandfather");
        boxGrmt.setTag("grandmother");
        boxAunt.setTag("aunt");
        boxUncle.setTag("uncle");
        boxCousin.setTag("cousin");

        // Lấy từ vựng đúng từ database
        DatabaseFamily1 dbHelper = new DatabaseFamily1(this);
        String[] randomWord = dbHelper.getRandomWord(); // ví dụ: ["grandfather", "ông"]
        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        // Sự kiện click chọn box
        View.OnClickListener boxClickListener = v -> {
            if (checkButton.getText().equals("Tiếp tục")) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxGr.setOnClickListener(boxClickListener);
        boxGrft.setOnClickListener(boxClickListener);
        boxGrmt.setOnClickListener(boxClickListener);
        boxAunt.setOnClickListener(boxClickListener);
        boxUncle.setOnClickListener(boxClickListener);
        boxCousin.setOnClickListener(boxClickListener);

        // Nút kiểm tra
        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (selectedBox.getTag().toString().equals(correctEnglish)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                checkButton.setText("Tiếp tục");
                disableBoxes();

                checkButton.setOnClickListener(view -> {
                    Intent intent = new Intent(FamilyActivity1.this, FamilyActivity4.class); // chuyển sang màn khác
                    startActivity(intent);
                    finish();
                });

            } else {
                Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(FamilyActivity1.this, TopicActivityEnglish.class));
            finish();
        });

        // Nút phát âm
        speakerButton.setOnClickListener(v -> playAudio(correctEnglish));
    }

    private void highlightSelected(LinearLayout selected) {
        boxGr.setBackground(null);
        boxGrft.setBackground(null);
        boxGrmt.setBackground(null);
        boxAunt.setBackground(null);
        boxUncle.setBackground(null);
        boxCousin.setBackground(null);

        selected.setBackgroundResource(R.drawable.language_item_bg);
        checkButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
    }

    private void disableBoxes() {
        boxGr.setClickable(false);
        boxGrft.setClickable(false);
        boxGrmt.setClickable(false);
        boxAunt.setClickable(false);
        boxUncle.setClickable(false);
        boxCousin.setClickable(false);
    }

    private void playAudio(String fileName) {
        if (mediaPlayer != null) mediaPlayer.release();

        int resId = getResources().getIdentifier(fileName, "raw", getPackageName());
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
