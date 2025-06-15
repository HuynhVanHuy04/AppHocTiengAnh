package com.example.doan;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;


public class SelectLanguageActivity extends AppCompatActivity {

    ListView languageListView;
    Button continueButton;
    String selectedLanguage = "";

    String[] languages = {"Tiếng Anh", "Tiếng Hoa", "Tiếng Ý", "Tiếng Pháp", "Tiếng Nhật"};
    int[] flags = {R.drawable.ic_usa, R.drawable.ic_cn, R.drawable.ic_y, R.drawable.ic_phap, R.drawable.ic_nhat};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        languageListView = findViewById(R.id.languageListView);
        continueButton = findViewById(R.id.btn_continue);

        LanguageAdapter adapter = new LanguageAdapter(this, languages, flags);
        languageListView.setAdapter(adapter);

        languageListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedLanguage = languages[position];
            continueButton.setEnabled(true);
            continueButton.setBackgroundTintList(ContextCompat.getColorStateList(SelectLanguageActivity.this, R.color.green));

            adapter.setSelectedPosition(position);

        });

        continueButton.setOnClickListener(v -> {
            if (selectedLanguage == null || selectedLanguage.isEmpty()) {
                Toast.makeText(this, "⚠️ Hãy chọn một ngôn ngữ trước khi tiếp tục!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn đã chọn: " + selectedLanguage, Toast.LENGTH_SHORT).show();

                Intent intent;
                switch (selectedLanguage) {
                    case "Tiếng Anh":
                        intent = new Intent(this, TopicActivityEnglish.class);
                        break;
                    case "Tiếng Hoa":
                       intent = new Intent(this, TopicActivityEnglish.class);
                        break;
                    case "Tiếng Ý":
                       intent = new Intent(this, ItalianTopicActivity.class);
                        break;
                    case "Tiếng Pháp":
                        intent = new Intent(this, FrenchTopicActivity.class);
                        break;
                    case "Tiếng Nhật":
                        intent = new Intent(this, JapaneseTopicActivity.class);
                        break;
                    default:
                        intent = new Intent(this, TopicActivityEnglish.class); // Phòng ngừa lỗi
                }

                startActivity(intent);
            }
        });



        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về màn hình trước đó
                Intent intent = new Intent(SelectLanguageActivity.this, IntroActivity.class);
                startActivity(intent);
                finish(); // Kết thúc IntroActivity để không quay lại bằng nút back
            }
        });



        ImageView imageView = findViewById(R.id.duoIcon);
        Glide.with(this)
                .asGif()
                .load(R.drawable.anh2) // tên file gif không cần đuôi .gif
                .into(imageView);

    }
}

