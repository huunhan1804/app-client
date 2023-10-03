package com.example.dietarysupplementshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.network.LoginManager;
import com.example.dietarysupplementshop.responses.LoginResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button buttonSignIn;
    private ImageButton btnGoogleSignIn;

    private Button buttonSignUpPage;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private EditText editTextEmail, editTextPassword;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Khởi tạo GoogleSignInOptions
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> {
                    // Xử lý khi có lỗi kết nối
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Thêm phần này để khởi tạo EditText
        editTextEmail = findViewById(R.id.editTextEmail);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = editTextEmail.getText().toString();
                if (!Validation.isValidUsernameOrEmailOrPhone(email)) {
                    textInputLayoutEmail.setError("Invalid username or phone number or email");
                } else {
                    textInputLayoutEmail.setError(null);
                }
            }
        });

        editTextPassword = findViewById(R.id.editTextPassword);
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

        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(view -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // handle sign in with email và password ở đây
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (Validation.isValidEmail(email) && Validation.isValidPassword(password)) {
                // Gửi email và password cho API
                LoginManager.getInstance().login(email, password, new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            // Đăng nhập thành công, xử lý kết quả ở đây
                            LoginResponse loginResponse = response.body();
                            String newAccessToken = loginResponse.getData().getAccessToken();
                            String newRefreshToken = loginResponse.getData().getRefreshToken();
                            // Lưu accessToken mới
                            saveTokenToSharedPreferences(newAccessToken);
                            saveRefreshTokenToSharedPreferences(newRefreshToken);

                            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                            startActivity(intent);
                            finish(); // Đóng Activity hiện tại
                        } else {
                            // Đăng nhập thất bại, xử lý lỗi ở đây
                            int errorCode = response.code(); // Mã trạng thái HTTP
                            String errorMessage = "Đăng nhập thất bại"; // Tin nhắn mặc định

                            if (response.errorBody() != null) {
                                try {
                                    // Thử lấy nội dung lỗi từ phản hồi JSON nếu có
                                    errorMessage = response.errorBody().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Hiển thị thông báo lỗi cho người dùng
                            Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Xử lý lỗi kết nối đến API ở đây
                        Toast.makeText(SignInActivity.this, "Kết nối đến máy chủ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Hiển thị thông báo lỗi cho người dùng
                if (!Validation.isValidEmail(email)) {
                    editTextEmail.setError("Email không hợp lệ");
                }
                if (!Validation.isValidPassword(password)) {
                    editTextPassword.setError("Mật khẩu không hợp lệ");
                }
            }
        });

        buttonSignUpPage = findViewById(R.id.buttonSignUpPage);
        buttonSignUpPage.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        });
    }

    // Hàm lưu refreshToken vào SharedPreferences
    private void saveRefreshTokenToSharedPreferences(String refreshToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    // Hàm lưu token vào SharedPreferences
    private void saveTokenToSharedPreferences(String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.apply();
    }

    // Hàm lấy token từ SharedPreferences
    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String googleUserName = account.getDisplayName();
            String googleEmail = account.getEmail();
            // Xử lý thông tin đăng nhập bằng Google ở đây
        } else {
            Toast.makeText(this, "Đăng nhập bằng Google thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotPasswordClick(View view) {
        Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(forgotPasswordIntent);
    }
}
