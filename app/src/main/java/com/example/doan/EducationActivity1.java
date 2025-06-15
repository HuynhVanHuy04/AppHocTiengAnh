package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;

public class EducationActivity1 extends Activity {

    private LinearLayout boxStudent, boxTeacher, boxRuler, boxBackpack, boxBook, boxPen;
    private ImageView imgStudent, imgTeacher, imgRuler, imgBackpack, imgBook, imgPen;
    private TextView wordText;
    private Button checkButton;
    private ImageView speakerButton;
    private String correctEnglish;
    private LinearLayout selectedBox = null;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education1);

        // Ánh xạ view
        boxStudent = findViewById(R.id.boxStudent);
        boxTeacher = findViewById(R.id.boxTeacher);
        boxRuler = findViewById(R.id.boxRuler);
        boxBackpack = findViewById(R.id.boxBackpack);
        boxBook = findViewById(R.id.boxBook);
        boxPen = findViewById(R.id.boxPen);

        imgStudent = findViewById(R.id.imgStudent);
        imgTeacher = findViewById(R.id.imgTeacher);
        imgRuler = findViewById(R.id.imgRuler);
        imgBackpack = findViewById(R.id.imgBackpack);
        imgBook = findViewById(R.id.imgBook);
        imgPen = findViewById(R.id.imgPen);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        // Gán tag để so sánh
        boxStudent.setTag("student");
        boxTeacher.setTag("teacher");
        boxRuler.setTag("ruler");
        boxBackpack.setTag("backpack");
        boxBook.setTag("book");
        boxPen.setTag("pen");

        // Lấy từ vựng đúng từ database
        DatabaseEdu1 dbHelper = new DatabaseEdu1(this);
        String[] randomWord = dbHelper.getRandomWord();
        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        // Sự kiện click chọn box
        View.OnClickListener boxClickListener = v -> {
            if (checkButton.getText().equals("Tiếp tục")) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxStudent.setOnClickListener(boxClickListener);
        boxTeacher.setOnClickListener(boxClickListener);
        boxRuler.setOnClickListener(boxClickListener);
        boxBackpack.setOnClickListener(boxClickListener);
        boxBook.setOnClickListener(boxClickListener);
        boxPen.setOnClickListener(boxClickListener);

        // Nút kiểm tra
        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (selectedBox.getTag().toString().equals(correctEnglish)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                checkButton.setText("Tiếp tục");
                disableBoxes();

                checkButton.setOnClickListener(view -> {
                    Intent intent = new Intent(EducationActivity1.this, EducationActivity2.class);
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
            startActivity(new Intent(EducationActivity1.this, TopicActivityEnglish.class));
            finish();
        });

        // Nút phát âm
        speakerButton.setOnClickListener(v -> playAudio(correctEnglish));
    }

    private void highlightSelected(LinearLayout selected) {
        boxStudent.setBackground(null);
        boxTeacher.setBackground(null);
        boxRuler.setBackground(null);
        boxBackpack.setBackground(null);
        boxBook.setBackground(null);
        boxPen.setBackground(null);

        selected.setBackgroundResource(R.drawable.language_item_bg);
        checkButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
    }

    private void disableBoxes() {
        boxStudent.setClickable(false);
        boxTeacher.setClickable(false);
        boxRuler.setClickable(false);
        boxBackpack.setClickable(false);
        boxBook.setClickable(false);
        boxPen.setClickable(false);
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
