package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;
import com.example.dietarysupplementshop.services.AuthService;
import com.example.dietarysupplementshop.services.OtpService;
import com.example.dietarysupplementshop.token.TokenManager;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.Response;

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
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        OtpService otpService = new OtpService();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        AuthService authService = new AuthService(tokenManager);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        buttonVerify = findViewById(R.id.buttonVerify);
        buttonResendCode = findViewById(R.id.buttonResendCode);
        countdownText = findViewById(R.id.countdown_text);
        textInputLayoutOTPCode = findViewById(R.id.textInputLayoutOTPCode);
        editTextOTPCode = findViewById(R.id.editTextOTPCode);

        timeLeftInMillis = COUNTDOWN_TIME;
        startCountdown();

        buttonResendCode.setOnClickListener(v -> {
            showProgressBar();
            otpService.sendOtpRegistration(email, new OtpService.OtpCallback() {
                @Override
                public void onSuccess(String successMessage) {
                    hideProgressBar();
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    startCountdown();
                }

                @Override
                public void onError(String errorMessage) {
                    hideProgressBar();
                    if(Integer.parseInt(errorMessage) == 409){
                        Toast.makeText(getApplicationContext(), "Email is already exist!", Toast.LENGTH_SHORT).show();
                    }  else if (Integer.parseInt(errorMessage) == 429){
                        Toast.makeText(getApplicationContext(), "Otp is already sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

        buttonVerify.setOnClickListener(v -> {
            String otp = editTextOTPCode.getText().toString();
            if(Validation.isValidOTP(otp)){
                showProgressBar();
                authService.registration(email, password, name, otp, new AuthService.AuthCallback() {
                    @Override
                    public void onSuccess(String successMessage){
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                textInputLayoutOTPCode.setError("OTP code must be 6 digits.");
            }
        });
    }

    private void startCountdown() {
        buttonResendCode.setVisibility(View.GONE);
        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                buttonResendCode.setVisibility(View.VISIBLE);
                countdownText.setText("");
            }
        }.start();
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d", seconds) + "s";
        countdownText.setText(timeLeftFormatted);
    }

    public void showProgressBar() {
        FrameLayout frameLayout = findViewById(R.id.frameLoading);
        LottieAnimationView animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        FrameLayout frameLayout = findViewById(R.id.frameLoading);
        LottieAnimationView animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);
    }
}