package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;

public class EducationActivity3 extends Activity {

    private LinearLayout boxDesk, boxEraser, boxPlaygound, boxLaboratory, boxChalk, boxResearcher;
    private ImageView imgDesk, imgEraser, imgPlaygound, imgLaboratory, imgChalk, imgResearcher;
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
        setContentView(R.layout.activity_education3); // Sửa nếu tên layout khác

        // Ánh xạ các LinearLayout
        boxDesk = findViewById(R.id.boxDesk);
        boxEraser = findViewById(R.id.boxEraser);
        boxPlaygound = findViewById(R.id.boxPlaygound);
        boxLaboratory = findViewById(R.id.boxLaboratory);
        boxChalk = findViewById(R.id.boxChalk);
        boxResearcher = findViewById(R.id.boxResearcher);

        // Ánh xạ các ImageView
        imgDesk = findViewById(R.id.imgDesk);
        imgEraser = findViewById(R.id.imgEraser);
        imgPlaygound = findViewById(R.id.imgPlaygound);
        imgLaboratory = findViewById(R.id.imgLaboratory);
        imgChalk = findViewById(R.id.imgChalk);
        imgResearcher = findViewById(R.id.imgResearcher);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        // Lấy từ vựng ngẫu nhiên
        DatabaseEdu3 dbHelper = new DatabaseEdu3(this);
        String[] randomWord = dbHelper.getRandomWord();
        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        // Gán tag cho từng box
        boxDesk.setTag("desk");
        boxEraser.setTag("eraser");
        boxPlaygound.setTag("playground");
        boxLaboratory.setTag("laboratory");
        boxChalk.setTag("chalk");
        boxResearcher.setTag("researcher");

        // Xác định box đúng
        correctBox = getBoxByWord(correctEnglish);

        // Xử lý sự kiện click box
        View.OnClickListener boxClickListener = v -> {
            if (checkButton.getText().equals("Tiếp tục")) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxDesk.setOnClickListener(boxClickListener);
        boxEraser.setOnClickListener(boxClickListener);
        boxPlaygound.setOnClickListener(boxClickListener);
        boxLaboratory.setOnClickListener(boxClickListener);
        boxChalk.setOnClickListener(boxClickListener);
        boxResearcher.setOnClickListener(boxClickListener);

        // Xử lý nút kiểm tra
        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (selectedBox.equals(correctBox)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                checkButton.setText("Tiếp tục");
                disableBoxes();
                checkButton.setOnClickListener(view -> {
                    Intent intent = new Intent(EducationActivity3.this, EducationActivity4.class);
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
            startActivity(new Intent(EducationActivity3.this, EducationActivity2.class));
            finish();
        });

        // Nút phát âm
        speakerButton.setOnClickListener(v -> playAudio(correctEnglish));
    }

    private LinearLayout getBoxByWord(String word) {
        switch (word) {
            case "desk": return boxDesk;
            case "eraser": return boxEraser;
            case "playground": return boxPlaygound;
            case "laboratory": return boxLaboratory;
            case "chalk": return boxChalk;
            case "researcher": return boxResearcher;
            default: return null;
        }
    }

    private void highlightSelected(LinearLayout selected) {
        // Reset background
        boxDesk.setBackground(null);
        boxEraser.setBackground(null);
        boxPlaygound.setBackground(null);
        boxLaboratory.setBackground(null);
        boxChalk.setBackground(null);
        boxResearcher.setBackground(null);

        selected.setBackgroundResource(R.drawable.language_item_bg);
        checkButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
    }

    private void disableBoxes() {
        boxDesk.setClickable(false);
        boxEraser.setClickable(false);
        boxPlaygound.setClickable(false);
        boxLaboratory.setClickable(false);
        boxChalk.setClickable(false);
        boxResearcher.setClickable(false);
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
