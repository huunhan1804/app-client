package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton btnGoogleSignIn;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private Button buttonSignInPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(view -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        editTextName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Xử lý sự kiện khi người dùng nhấn nút "Sign Up"
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin từ các EditText
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (isValidEmail(email) && isValidPassword(password) && isValidName(name)) {
                    // Name hợp lệ, thực hiện xử lý đăng ký ở đây (ví dụ: gửi dữ liệu đăng ký lên máy chủ)

                    // Chuyển người dùng đến trang đăng nhập hoặc màn hình chính
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại
                } else {
                    // Hiển thị thông báo lỗi cho người dùng
                    if (!isValidEmail(email)) {
                        editTextEmail.setError("Email không hợp lệ");
                    }
                    if (!isValidPassword(password)) {
                        editTextPassword.setError("Mật khẩu không hợp lệ");
                    }

                    if (!isValidName(name)) {
                        editTextName.setError("Tên không được để trống");
                    }
                }
            }
        });


        buttonSignInPage = findViewById(R.id.buttonSignInPage);
        buttonSignInPage.setOnClickListener(view -> {
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivity(signInIntent);
        });
    }

    private boolean isValidName(String name) {
        // Kiểm tra nếu Name không rỗng và không chỉ chứa khoảng trắng
        return !TextUtils.isEmpty(name) && !name.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        return email.matches(emailPattern);
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
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
}