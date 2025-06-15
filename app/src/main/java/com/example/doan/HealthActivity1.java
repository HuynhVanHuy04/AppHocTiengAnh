package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;

public class HealthActivity1 extends Activity {

    private LinearLayout boxHeadache, boxSneezing, boxCoughing, boxSorethroat, boxRunnynose, boxFever;
    private TextView wordText;
    private Button checkButton;
    private ImageView speakerButton;
    private String correctEnglish;
    private LinearLayout selectedBox = null;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health1);

        // Ánh xạ view
        boxHeadache = findViewById(R.id.boxHeadache);
        boxSneezing = findViewById(R.id.boxSneezing);
        boxCoughing = findViewById(R.id.boxCoughing);
        boxSorethroat = findViewById(R.id.boxSorethroat);
        boxRunnynose = findViewById(R.id.boxRunnynose);
        boxFever = findViewById(R.id.boxFever);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        // Gán tag để so sánh
        boxHeadache.setTag("headache");
        boxSneezing.setTag("sneezing");
        boxCoughing.setTag("coughing");
        boxSorethroat.setTag("sore throat");
        boxRunnynose.setTag("runny nose");
        boxFever.setTag("fever");

        // Lấy từ vựng ngẫu nhiên từ database
        DatabaseHealth1 dbHelper = new DatabaseHealth1(this);
        String[] randomWord = dbHelper.getRandomWord(); // ví dụ: ["headache", "đau đầu"]

        if (randomWord == null || randomWord[0] == null) {
            Toast.makeText(this, "Không lấy được dữ liệu từ database", Toast.LENGTH_LONG).show();
            finish(); // Quay lại activity trước
            return;
        }

        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        // Sự kiện chọn hình
        View.OnClickListener boxClickListener = v -> {
            if (checkButton.getText().equals("Tiếp tục")) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxHeadache.setOnClickListener(boxClickListener);
        boxSneezing.setOnClickListener(boxClickListener);
        boxCoughing.setOnClickListener(boxClickListener);
        boxSorethroat.setOnClickListener(boxClickListener);
        boxRunnynose.setOnClickListener(boxClickListener);
        boxFever.setOnClickListener(boxClickListener);

        // Nút kiểm tra
        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (selectedBox.getTag().toString().equals(correctEnglish)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                checkButton.setText("Tiếp tục");
                disableBoxes();

                checkButton.setOnClickListener(view -> {
                    Intent intent = new Intent(HealthActivity1.this, HealthActivity2.class);
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
            startActivity(new Intent(HealthActivity1.this, TopicActivityEnglish.class));
            finish();
        });

        // Nút phát âm
        speakerButton.setOnClickListener(v -> playAudio(correctEnglish));
    }

    private void highlightSelected(LinearLayout selected) {
        boxHeadache.setBackground(null);
        boxSneezing.setBackground(null);
        boxCoughing.setBackground(null);
        boxSorethroat.setBackground(null);
        boxRunnynose.setBackground(null);
        boxFever.setBackground(null);

        selected.setBackgroundResource(R.drawable.language_item_bg);
        checkButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
    }

    private void disableBoxes() {
        boxHeadache.setClickable(false);
        boxSneezing.setClickable(false);
        boxCoughing.setClickable(false);
        boxSorethroat.setClickable(false);
        boxRunnynose.setClickable(false);
        boxFever.setClickable(false);
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
