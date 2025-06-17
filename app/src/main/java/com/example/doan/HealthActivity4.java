package com.example.doan;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class HealthActivity4 extends AppCompatActivity {

    LinearLayout englishColumn, vietnameseColumn;
    DatabaseHealth4 dbHelper;
    List<Word> wordList;
    Map<String, String> wordMap = new HashMap<>();
    String selectedEnglish = null;
    Button lastSelectedButton = null;
    Button continueButton;
    int correctCount = 0;
    int totalPairs;

    // Biến thống kê
    int xp, correct, total;
    long startTime;
    boolean finishedCorrectly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health4);

        // Nhận dữ liệu từ HealthActivity3
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        englishColumn = findViewById(R.id.englishColumn);
        vietnameseColumn = findViewById(R.id.vietnameseColumn);
        continueButton = findViewById(R.id.btn_continue);

        dbHelper = new DatabaseHealth4(this);
        wordList = dbHelper.getRandomWords(7);
        totalPairs = wordList.size();

        for (Word w : wordList) {
            wordMap.put(w.getEnglish(), w.getVietnamese());
        }

        setupButtons();

        continueButton.setEnabled(false);

        continueButton.setOnClickListener(v -> {
            Intent nextIntent = new Intent(HealthActivity4.this, HealthActivity5.class);

            // Nếu làm đúng toàn bộ thì mới tăng xp và correct
            if (finishedCorrectly) {
                xp += 15;
                correct++;
                total++;
            }

            nextIntent.putExtra("xp", xp);
            nextIntent.putExtra("correct", correct);
            nextIntent.putExtra("total", total);
            nextIntent.putExtra("startTime", startTime);
            startActivity(nextIntent);
            finish();
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(HealthActivity4.this, HealthActivity3.class);
            backIntent.putExtra("xp", xp);
            backIntent.putExtra("correct", correct);
            backIntent.putExtra("total", total);
            backIntent.putExtra("startTime", startTime);
            startActivity(backIntent);
            finish();
        });
    }

    private void setupButtons() {
        for (Word w : wordList) {
            Button btn = createButton(w.getEnglish());
            btn.setOnClickListener(v -> {
                selectedEnglish = btn.getText().toString();
                if (lastSelectedButton != null) {
                    lastSelectedButton.setBackground(createNormalBackground());
                }
                btn.setBackground(createSelectedBackground());
                lastSelectedButton = btn;
            });
            englishColumn.addView(btn);
        }

        List<String> shuffledVietnamese = new ArrayList<>();
        for (Word w : wordList) {
            shuffledVietnamese.add(w.getVietnamese());
        }
        Collections.shuffle(shuffledVietnamese);

        for (String vn : shuffledVietnamese) {
            Button btn = createButton(vn);
            btn.setOnClickListener(v -> {
                if (selectedEnglish == null) return;

                String correctVN = wordMap.get(selectedEnglish);
                if (vn.equals(correctVN)) {
                    btn.setAlpha(0.3f);
                    btn.setEnabled(false);

                    for (int i = 0; i < englishColumn.getChildCount(); i++) {
                        Button b = (Button) englishColumn.getChildAt(i);
                        if (b.getText().toString().equals(selectedEnglish)) {
                            b.setAlpha(0.3f);
                            b.setEnabled(false);
                            b.setBackground(createNormalBackground());
                            break;
                        }
                    }

                    correctCount++;

                    if (correctCount == totalPairs) {
                        finishedCorrectly = true;
                        continueButton.setBackgroundTintList(getColorStateList(android.R.color.holo_green_dark));
                        continueButton.setEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Sai rồi!", Toast.LENGTH_SHORT).show();
                }

                selectedEnglish = null;
                if (lastSelectedButton != null) {
                    lastSelectedButton.setBackground(createNormalBackground());
                    lastSelectedButton = null;
                }
            });
            vietnameseColumn.addView(btn);
        }
    }

    private Button createButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setAllCaps(false);
        btn.setTextSize(16f);
        btn.setPadding(16, 16, 16, 16);
        btn.setLayoutParams(getLayoutParams());
        btn.setBackground(createNormalBackground());
        return btn;
    }

    private GradientDrawable createNormalBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(16);
        drawable.setStroke(3, 0xFFCCCCCC);
        drawable.setColor(0xFFFFFFFF);
        return drawable;
    }

    private GradientDrawable createSelectedBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(16);
        drawable.setStroke(5, 0xFF4CAF50);
        drawable.setColor(0xFFFFFFFF);
        return drawable;
    }

    private LinearLayout.LayoutParams getLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 16, 8, 16);
        return params;
    }
}
