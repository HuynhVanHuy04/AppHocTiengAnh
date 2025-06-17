package com.example.doan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.content.ContextCompat;

public class FamilyActivity3 extends Activity {

    private LinearLayout boxLook, boxBringUp, boxSon, boxWife, boxDaughter, boxDistant;
    private TextView wordText;
    private Button checkButton;
    private ImageView speakerButton;
    private String correctEnglish;
    private LinearLayout selectedBox = null;
    private LinearLayout correctBox = null;
    private MediaPlayer mediaPlayer;

    // Thông tin thống kê
    private int xp = 0;
    private int correct = 0;
    private int total = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family3);

        // Nhận dữ liệu từ FamilyActivity2
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        boxLook = findViewById(R.id.boxLook);
        boxBringUp = findViewById(R.id.boxBringUp);
        boxSon = findViewById(R.id.boxSon);
        boxWife = findViewById(R.id.boxWife);
        boxDaughter = findViewById(R.id.boxDaughter);
        boxDistant = findViewById(R.id.boxDistant);

        wordText = findViewById(R.id.wordText);
        checkButton = findViewById(R.id.checkButton);
        speakerButton = findViewById(R.id.speakerButton);

        DatabaseFamily3 dbHelper = new DatabaseFamily3(this);
        String[] randomWord = dbHelper.getRandomWord();
        correctEnglish = randomWord[0].trim().toLowerCase();
        wordText.setText(correctEnglish);

        boxLook.setTag("child");
        boxBringUp.setTag("niece");
        boxSon.setTag("son");
        boxWife.setTag("wife");
        boxDaughter.setTag("daughter");
        boxDistant.setTag("distant");

        correctBox = getBoxByWord(correctEnglish);

        View.OnClickListener boxClickListener = v -> {
            if (checkButton.getText().equals("Tiếp tục")) return;
            selectedBox = (LinearLayout) v;
            highlightSelected(selectedBox);
        };

        boxLook.setOnClickListener(boxClickListener);
        boxBringUp.setOnClickListener(boxClickListener);
        boxSon.setOnClickListener(boxClickListener);
        boxWife.setOnClickListener(boxClickListener);
        boxDaughter.setOnClickListener(boxClickListener);
        boxDistant.setOnClickListener(boxClickListener);

        checkButton.setOnClickListener(v -> {
            if (selectedBox == null) {
                Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (selectedBox.equals(correctBox)) {
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                checkButton.setText("Tiếp tục");
                disableBoxes();

                // Sự kiện sau khi đúng -> chuyển tiếp và gửi dữ liệu
                checkButton.setOnClickListener(view -> {
                    xp += 11;
                    correct += 1;
                    total += 1;

                    Intent nextIntent = new Intent(FamilyActivity3.this, FamilyActivity6.class);
                    nextIntent.putExtra("xp", xp);
                    nextIntent.putExtra("correct", correct);
                    nextIntent.putExtra("total", total);
                    nextIntent.putExtra("startTime", startTime);
                    startActivity(nextIntent);
                    finish();
                });

            } else {
                Toast.makeText(this, "Sai rồi 😢", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(FamilyActivity3.this, FamilyActivity2.class));
            finish();
        });

        speakerButton.setOnClickListener(v -> playAudio(correctEnglish));
    }

    private LinearLayout getBoxByWord(String word) {
        switch (word) {
            case "child": return boxLook;
            case "niece": return boxBringUp;
            case "son": return boxSon;
            case "wife": return boxWife;
            case "daughter": return boxDaughter;
            case "distant": return boxDistant;
            default: return null;
        }
    }

    private void highlightSelected(LinearLayout selected) {
        boxLook.setBackground(null);
        boxBringUp.setBackground(null);
        boxSon.setBackground(null);
        boxWife.setBackground(null);
        boxDaughter.setBackground(null);
        boxDistant.setBackground(null);

        selected.setBackgroundResource(R.drawable.language_item_bg);
        checkButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
    }

    private void disableBoxes() {
        boxLook.setClickable(false);
        boxBringUp.setClickable(false);
        boxSon.setClickable(false);
        boxWife.setClickable(false);
        boxDaughter.setClickable(false);
        boxDistant.setClickable(false);
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
