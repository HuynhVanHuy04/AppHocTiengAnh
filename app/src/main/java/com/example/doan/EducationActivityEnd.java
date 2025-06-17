package com.example.doan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EducationActivityEnd extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_end);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvSubtitle = findViewById(R.id.tv_subtitle);
//        Button btnGetXP = findViewById(R.id.btn_get_xp);

        // Nhận dữ liệu từ EducationActivity7
        int xp = getIntent().getIntExtra("xp", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 0);
        long startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        // Tính thời gian hoàn thành
        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;
        int seconds = (int) (durationMillis / 1000);
        String formattedTime = String.format("%d:%02d", seconds / 60, seconds % 60);

        // Tính độ chính xác
        int accuracy = total > 0 ? (int) ((correct * 100.0f) / total) : 0;
        int mistakes = total - correct;

        // Hiển thị tiêu đề và mô tả
        tvSubtitle.setText("Bạn đã sửa " + mistakes + " lỗi sai. Vững bước tiến lên!");

        // Cập nhật các thống kê
        TextView tvXP = findViewById(R.id.tv_xp);
        TextView tvTime = findViewById(R.id.tv_time);
        TextView tvAccuracy = findViewById(R.id.tv_accuracy);

        if (tvXP != null) tvXP.setText(String.valueOf(xp));
        if (tvTime != null) tvTime.setText(formattedTime);
        if (tvAccuracy != null) tvAccuracy.setText(accuracy + "%");

        // Xử lý nút Nhận KN
//        btnGetXP.setOnClickListener(v -> {
//            Toast.makeText(this, "🎉 Nhận " + xp + " điểm kinh nghiệm!", Toast.LENGTH_SHORT).show();
//            finish(); // hoặc chuyển về MainActivity nếu cần
//        });
    }
}
