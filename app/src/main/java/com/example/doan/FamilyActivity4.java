package com.example.doan;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class FamilyActivity4 extends AppCompatActivity {

    LinearLayout englishColumn, vietnameseColumn;
    DatabaseFamilylist4 dbHelper;
    List<Word> wordList;
    Map<String, String> wordMap = new HashMap<>();
    String selectedEnglish = null;
    Button lastSelectedButton = null;
    Button continueButton;
    int correctCount = 0;
    int totalPairs;

    // Thông tin thống kê
    int xp = 0;
    int correct = 0;
    int total = 0;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family4);

        // Nhận dữ liệu từ FamilyActivity3
        Intent intent = getIntent();
        xp = intent.getIntExtra("xp", 0);
        correct = intent.getIntExtra("correct", 0);
        total = intent.getIntExtra("total", 0);
        startTime = intent.getLongExtra("startTime", System.currentTimeMillis());

        englishColumn = findViewById(R.id.englishColumn);
        vietnameseColumn = findViewById(R.id.vietnameseColumn);
        continueButton = findViewById(R.id.btn_continue);

        dbHelper = new DatabaseFamilylist4(this);
        wordList = dbHelper.getRandomWords(7);
        totalPairs = wordList.size();

        for (Word w : wordList) {
            wordMap.put(w.getEnglish(), w.getVietnamese());
        }

        setupButtons();

        continueButton.setEnabled(false);  // Disable ban đầu

        continueButton.setOnClickListener(v -> {
            // Cập nhật thống kê
            xp += 12;
            correct += totalPairs;
            total += totalPairs;

            Intent nextIntent = new Intent(FamilyActivity4.this, FamilyActivity5.class);
            nextIntent.putExtra("xp", xp);
            nextIntent.putExtra("correct", correct);
            nextIntent.putExtra("total", total);
            nextIntent.putExtra("startTime", startTime);
            startActivity(nextIntent);
            finish();
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent backIntent = new Intent(FamilyActivity4.this, FamilyActivity3.class);
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
        drawable.setStroke(3, 0xFFCCCCCC); // gray border
        drawable.setColor(0xFFFFFFFF);
        return drawable;
    }

    private GradientDrawable createSelectedBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(16);
        drawable.setStroke(5, 0xFF4CAF50); // green border
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
