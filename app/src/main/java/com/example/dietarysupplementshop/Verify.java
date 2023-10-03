package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Verify extends AppCompatActivity {

    private Button buttonVerify;
    private Button buttonResendCode;
    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis; // Thời gian còn lại (milliseconds)
    private static final long COUNTDOWN_TIME = 60000; // Thời gian chờ (60 giây)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // Ánh xạ các thành phần giao diện
        buttonVerify = findViewById(R.id.buttonVerify);
        buttonResendCode = findViewById(R.id.buttonResendCode);
        countdownText = findViewById(R.id.countdown_text);

        // Khởi tạo CountDownTimer
        timeLeftInMillis = COUNTDOWN_TIME;
        startCountdown();

        // Thiết lập sự kiện click cho nút "Re-send OTP"
        buttonResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic gửi lại mã OTP ở đây
                // Sau khi gửi lại thành công, bắt đầu lại đếm ngược
                startCountdown();
            }
        });
    }

    private void startCountdown() {
        // Tạo CountDownTimer mới với thời gian chờ COUNTDOWN_TIME
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Cập nhật thời gian còn lại và hiển thị lên giao diện
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                // Khi đếm ngược kết thúc, ẩn nút "Verify" và hiện nút "Re-send OTP"
                buttonVerify.setVisibility(View.INVISIBLE);
                buttonResendCode.setVisibility(View.VISIBLE);
                countdownText.setText(""); // Đặt văn bản đếm ngược thành trống
            }
        }.start(); // Bắt đầu đếm ngược
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d", seconds) + "s";
        countdownText.setText(timeLeftFormatted);
    }
}