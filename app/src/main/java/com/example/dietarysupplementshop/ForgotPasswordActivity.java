package com.example.dietarysupplementshop;

import static com.example.dietarysupplementshop.constant.Validation.isValidEmail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.services.AuthService;
import com.example.dietarysupplementshop.services.OtpService;
import com.example.dietarysupplementshop.token.TokenManager;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageButton btnSendOTP;

    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfirmPassword, textInputLayoutOTPCode;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextOTPCode;
    private TextView textViewCountdown;

    private Button buttonSignUpPage;
    private Button buttonConfirm;
    private long timeLeftInMillis;
    private static final long COUNTDOWN_TIME = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        textViewCountdown = findViewById(R.id.textViewCountdown);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        buttonConfirm = findViewById(R.id.buttonConfirm);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        textInputLayoutOTPCode = findViewById(R.id.textInputLayoutOTPCode);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextOTPCode = findViewById(R.id.editTextOTPCode);

        OtpService otpService = new OtpService();
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        AuthService authService = new AuthService(tokenManager);

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
            showProgressBar();
            otpService.sendOtpForgotPassword(email, new OtpService.OtpCallback() {
                @Override
                public void onSuccess(String successMessage) {
                    hideProgressBar();
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    btnSendOTP.setVisibility(View.INVISIBLE);
                    textViewCountdown.setVisibility(View.VISIBLE);
                    startCountdown();
                }

                @Override
                public void onError(String errorMessage) {
                    hideProgressBar();
                    if(Integer.parseInt(errorMessage) == 404){
                        editTextEmail.setError("Account doesn't exist!");
                    }
                }
            });
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

        buttonConfirm.setOnClickListener(view -> {
            if(Validation.isValidOTP(editTextOTPCode.getText().toString())
                    && Validation.isValidPasswordMatch(editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString())
                    && Validation.isValidPassword(editTextPassword.getText().toString())
                    && Validation.isValidEmailOrPhone(editTextEmail.getText().toString())
            ) {
                showProgressBar();
                authService.forgotPassword(editTextEmail.getText().toString(), editTextConfirmPassword.getText().toString(), editTextOTPCode.getText().toString(), new AuthService.ForgotPasswordCallBack() {
                    @Override
                    public void onSuccess(String successMessage) {
                        hideProgressBar();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
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
