package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TopicActivityEnglish extends AppCompatActivity {

    LinearLayout educationLayout, familyLayout, healthLayout;
    Button btnContinue;

    String selectedTopic = ""; // Lưu chủ đề được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_english);

        educationLayout = findViewById(R.id.Education);
        familyLayout = findViewById(R.id.Family);
        healthLayout = findViewById(R.id.Health);
        btnContinue = findViewById(R.id.btn_continue);

        View.OnClickListener selectListener = view -> {
            // Reset viền trước
            clearSelection();

            // Đặt viền xanh lá cho mục được chọn
            view.setBackground(ContextCompat.getDrawable(TopicActivityEnglish.this, R.drawable.language_item_bg));

            // Gán tên chủ đề
            if (view.getId() == R.id.Education) {
                selectedTopic = "Education";
            } else if (view.getId() == R.id.Family) {
                selectedTopic = "Family";
            } else if (view.getId() == R.id.Health) {
                selectedTopic = "Health";
            }

            // Đổi màu nút tiếp tục thành xanh lá
            btnContinue.setEnabled(true);
            btnContinue.setBackgroundTintList(ContextCompat.getColorStateList(TopicActivityEnglish.this, R.color.green));
        };

        educationLayout.setOnClickListener(selectListener);
        familyLayout.setOnClickListener(selectListener);
        healthLayout.setOnClickListener(selectListener);

        btnContinue.setOnClickListener(v -> {
            if (selectedTopic.isEmpty()) {
                Toast.makeText(this, "⚠️ Hãy chọn một chủ đề trước khi tiếp tục!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Bạn đã chọn: " + selectedTopic, Toast.LENGTH_SHORT).show();

            Intent intent;
            switch (selectedTopic) {
                case "Education":
                    intent = new Intent(this, EducationActivity1.class);
                    break;
                case "Family":
                    intent = new Intent(this, FamilyActivity2.class); // Cần tạo lớp này
                    break;
                case "Health":
                    intent = new Intent(TopicActivityEnglish.this, HealthActivity1.class); // Cần tạo lớp này
                    break;
                default:
                    intent = new Intent(this, TopicActivityEnglish.class); // Phòng lỗi
            }

            startActivity(intent);
        });

        // Nút quay lại
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(TopicActivityEnglish.this, SelectLanguageActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void clearSelection() {
        educationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        familyLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        healthLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}
