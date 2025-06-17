package com.example.doan;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//import com.plattysoft.leonids.ParticleSystem;


import java.util.Random;

public class HealthActivityEnd extends AppCompatActivity {
//    private MediaPlayer fireworkSound;
//    private Handler handler = new Handler();
//
//    View fireworkCenter, fireworkLeft, fireworkRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_end);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvSubtitle = findViewById(R.id.tv_subtitle);

        int xp = getIntent().getIntExtra("xp", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 0);
        long startTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());

        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;
        int seconds = (int) (durationMillis / 1000);
        String formattedTime = String.format("%d:%02d", seconds / 60, seconds % 60);

        int accuracy = total > 0 ? (int) ((correct * 100.0f) / total) : 0;
        int mistakes = total - correct;

        tvSubtitle.setText("Bạn đã sửa " + mistakes + " lỗi sai. Vững bước tiến lên!");

        TextView tvXP = findViewById(R.id.tv_xp);
        TextView tvTime = findViewById(R.id.tv_time);
        TextView tvAccuracy = findViewById(R.id.tv_accuracy);

        if (tvXP != null) tvXP.setText(String.valueOf(xp));
        if (tvTime != null) tvTime.setText(formattedTime);
        if (tvAccuracy != null) tvAccuracy.setText(accuracy + "%");

//        fireworkCenter = findViewById(R.id.firework_placeholder);
//        fireworkLeft = findViewById(R.id.firework_left);
//        fireworkRight = findViewById(R.id.firework_right);
//
//        // Phát nhạc pháo hoa
//        fireworkSound = MediaPlayer.create(this, R.raw.firework);
//        fireworkSound.setLooping(false);
//        fireworkSound.start();

        // Bắn pháo nhiều đợt
//        shootFireworksRepeatedly();
   }

//    private void shootFireworksRepeatedly() {
//        int repeat = 5;
//        for (int i = 0; i < repeat; i++) {
//            final int delay = i * 1000; // mỗi đợt cách nhau 1 giây
//
//            handler.postDelayed(() -> {
//                Random rand = new Random();
//
//                // Bắn từ giữa
//                new ParticleSystem(this, 100, R.drawable.star, 1500)
//                        .setSpeedRange(0.2f + rand.nextFloat() * 0.3f, 0.5f + rand.nextFloat() * 0.3f)
//                        .oneShot(fireworkCenter, 100);
//
//                // Bắn từ trái
//                new ParticleSystem(this, 80, R.drawable.star, 1500)
//                        .setSpeedByComponentsRange(-0.3f, -0.1f, -0.2f, 0.2f)
//                        .oneShot(fireworkLeft, 80);
//
//                // Bắn từ phải
//                new ParticleSystem(this, 80, R.drawable.star, 1500)
//                        .setSpeedByComponentsRange(0.1f, 0.3f, -0.2f, 0.2f)
//                        .oneShot(fireworkRight, 80);
//            }, delay);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (fireworkSound != null) {
//            fireworkSound.stop();
//            fireworkSound.release();
//        }
//        handler.removeCallbacksAndMessages(null);
//        super.onDestroy();
//    }
}
