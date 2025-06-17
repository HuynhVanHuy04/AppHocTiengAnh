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

    // Biến thống kê
    private int xp = 0;
    private int correct = 0;
    private int total = 0;
    private long startTime;

    private boolean isCheckMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health1);

        // Nhận dữ liệu từ Intent trước đó (nếu có)
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

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
        String[] randomWord = dbHelper.getRandomWord();

        if (randomWord == null || randomWord[0] == null) {
            Toast.makeText(this, "Không lấy được dữ liệu từ database", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        // Sự kiện chọn hình
        View.OnClickListener boxClickListener = v -> {
            if (!isCheckMode) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxHeadache.setOnClickListener(boxClickListener);
        boxSneezing.setOnClickListener(boxClickListener);
        boxCoughing.setOnClickListener(boxClickListener);
        boxSorethroat.setOnClickListener(boxClickListener);
        boxRunnynose.setOnClickListener(boxClickListener);
        boxFever.setOnClickListener(boxClickListener);

        // Nút kiểm tra và tiếp tục
        checkButton.setOnClickListener(v -> {
            if (isCheckMode) {
                if (selectedBox == null) {
                    Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
                } else if (selectedBox.getTag().toString().equals(correctEnglish)) {
                    Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                    checkButton.setText("Tiếp tục");
                    disableBoxes();
                    isCheckMode = false;

                    // Cập nhật thống kê
                    xp += 15;
                    correct += 1;
                    total += 1;

                } else {
                    Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
                }
            } else {
                // ➤ Chuyển sang màn tiếp theo và gửi thống kê
                Intent nextIntent = new Intent(HealthActivity1.this, HealthActivity2.class);
                nextIntent.putExtra("xp", xp);
                nextIntent.putExtra("correct", correct);
                nextIntent.putExtra("total", total);
                nextIntent.putExtra("startTime", startTime);
                startActivity(nextIntent);
                finish();
            }
        });

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(HealthActivity1.this, TopicActivityEnglish.class);
            startActivity(backIntent);
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
