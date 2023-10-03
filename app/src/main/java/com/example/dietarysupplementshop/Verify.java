package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dietarysupplementshop.constant.Validation;
import com.google.android.material.textfield.TextInputLayout;

public class Verify extends AppCompatActivity {

    private Button buttonVerify;
    private Button buttonResendCode;

    private TextInputLayout textInputLayoutOTPCode;

    private EditText editTextOTPCode;

    private TextView countdownText;
    private long timeLeftInMillis;
    private static final long COUNTDOWN_TIME = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // Ánh xạ các thành phần giao diện
        buttonVerify = findViewById(R.id.buttonVerify);
        buttonResendCode = findViewById(R.id.buttonResendCode);
        countdownText = findViewById(R.id.countdown_text);
        textInputLayoutOTPCode = findViewById(R.id.textInputLayoutOTPCode);
        editTextOTPCode = findViewById(R.id.editTextOTPCode);

        // Khởi tạo CountDownTimer
        timeLeftInMillis = COUNTDOWN_TIME;
        startCountdown();

        buttonResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountdown();
            }
        });

        editTextOTPCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String otp = editTextOTPCode.getText().toString();
                if (!Validation.isValidOTP(otp)) {
                    textInputLayoutOTPCode.setError("OTP code must be 6 digits.");
                } else {
                    textInputLayoutOTPCode.setError(null);
                }
            }
        });

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editTextOTPCode.getText().toString();
                if(Validation.isValidOTP(otp)){
                    //Code logic
                } else {
                    textInputLayoutOTPCode.setError("OTP code must be 6 digits.");
                }
            }
        });
    }

    private void startCountdown() {
        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                buttonVerify.setVisibility(View.INVISIBLE);
                buttonResendCode.setVisibility(View.VISIBLE);
                countdownText.setText("");
            }
        }.start(); // Bắt đầu đếm ngược
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d", seconds) + "s";
        countdownText.setText(timeLeftFormatted);
    }
}