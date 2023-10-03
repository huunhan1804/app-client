package com.example.dietarysupplementshop;

import static com.example.dietarysupplementshop.constant.Validation.isValidEmail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.constant.Validation;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageButton btnSendOTP;

    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfirmPassword, textInputLayoutOTPCode;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextOTPCode;
    private TextView textViewCountdown;

    private Button buttonSignUpPage;
    private long timeLeftInMillis;
    private static final long COUNTDOWN_TIME = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        textViewCountdown = findViewById(R.id.textViewCountdown);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        textInputLayoutOTPCode = findViewById(R.id.textInputLayoutOTPCode);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextOTPCode = findViewById(R.id.editTextOTPCode);

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = editTextEmail.getText().toString();
                if (!Validation.isValidEmailOrPhone(email)) {
                    textInputLayoutEmail.setError("Invalid email or phone number");
                } else {
                    textInputLayoutEmail.setError(null);
                }
            }
        });

        btnSendOTP.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            if (!isValidEmail(email)) {
                editTextEmail.setError("Invalid email");
                return;
            }
            btnSendOTP.setVisibility(View.INVISIBLE);
            textViewCountdown.setVisibility(View.VISIBLE);
            startCountdown();
        });

        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = editTextPassword.getText().toString();
                if (!Validation.isValidPassword(password)) {
                    textInputLayoutPassword.setError("Password must be at least 8 characters long, including uppercase, lowercase, digits, and special characters.");
                } else {
                    textInputLayoutPassword.setError(null);
                }
            }
        });

        editTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                if (!Validation.isValidPasswordMatch(password, confirmPassword)) {
                    textInputLayoutConfirmPassword.setError("Passwords do not match.");
                } else {
                    textInputLayoutConfirmPassword.setError(null);
                }
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

        buttonSignUpPage = findViewById(R.id.buttonSignUpPage);
        buttonSignUpPage.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        });


    }

    private void startCountdown() {
        CountDownTimer countDownTimer;
        timeLeftInMillis = COUNTDOWN_TIME;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                textViewCountdown.setVisibility(View.INVISIBLE);
                btnSendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        @SuppressLint("DefaultLocale") String timeLeftFormatted = String.format("%02d", seconds) + "s";
        textViewCountdown.setText(timeLeftFormatted);
    }
}
