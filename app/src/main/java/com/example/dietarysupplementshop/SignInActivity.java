package com.example.dietarysupplementshop;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;
import com.example.dietarysupplementshop.services.AuthService;
import com.example.dietarysupplementshop.token.TokenManager;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private BeginSignInRequest signUpRequest;

    private Button buttonSignIn;
    private ImageButton btnGoogleSignIn;

    private Button buttonSignUpPage;

    private EditText editTextEmail, editTextPassword;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    private TokenManager tokenManager;
    private AuthService authService;

    private static final int REQ_GOOGLE_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tokenManager = new TokenManager(getApplicationContext());
        authService = new AuthService(tokenManager);

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
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi lại hàm đăng nhập One Tap mỗi khi nút btnGoogleSignIn được nhấn
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(SignInActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(
                                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                            null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.getLocalizedMessage());
                            }
                        });
            }
        });

        // handle sign in with email và password ở đây
        buttonSignIn = findViewById(R.id.buttonSignIn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_GOOGLE_SIGN_IN);
            }
        });



        buttonSignUpPage = findViewById(R.id.buttonSignUpPage);
        buttonSignUpPage.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        });


        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });

         CallbackManager callbackManager = CallbackManager.Factory.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        authService.authenticateGoogle(idToken, new AuthService.AuthCallback() {
                            @Override
                            public void onSuccess(String successMessage){
                                Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
            case REQ_GOOGLE_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if (account != null) {
                        String idToken = account.getIdToken();
                        authService.authenticateGoogle(idToken, new AuthService.AuthCallback() {
                            @Override
                            public void onSuccess(String successMessage){
                                Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                }
        }
    }




    public void forgotPasswordClick(View view) {
        Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(forgotPasswordIntent);
    }
}
