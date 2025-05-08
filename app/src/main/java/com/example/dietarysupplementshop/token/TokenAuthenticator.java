package com.example.dietarysupplementshop.token;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.SignInActivity;
import com.example.dietarysupplementshop.services.AuthService;


import java.util.Objects;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private static final AuthService authService = new AuthService(MyApplication.getInstance().getTokenManager());
    private static final Object lock = new Object();
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response){
        synchronized (lock) {
            String currentAccessToken = MyApplication.getInstance().getTokenManager().getAccessToken();
            String originalAccessToken = Objects.requireNonNull(response.request().header("Authorization")).replace("Bearer ", "");

            // Kiểm tra xem access token đã được refresh chưa
            if (!originalAccessToken.equals(currentAccessToken)) {
                return newRequestWithAccessToken(response, currentAccessToken);
            }

            String refreshToken = MyApplication.getInstance().getTokenManager().getRefreshToken();
            if (refreshToken != null) {
                boolean refreshResult = refreshToken(refreshToken);
                if (refreshResult) {
                    String newAccessToken = MyApplication.getInstance().getTokenManager().getAccessToken();
                    return newRequestWithAccessToken(response, newAccessToken);
                } else {
                    MyApplication.getInstance().getTokenManager().clearTokens();
                    Intent logoutIntent = new Intent(MyApplication.getInstance().getApplicationContext(), SignInActivity.class);
                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    MyApplication.getInstance().getApplicationContext().startActivity(logoutIntent);
                }
            } else {
                MyApplication.getInstance().getTokenManager().clearTokens();
                Intent logoutIntent = new Intent(MyApplication.getInstance().getApplicationContext(), SignInActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                MyApplication.getInstance().getApplicationContext().startActivity(logoutIntent);
            }
            return null;
        }
    }

    private Request newRequestWithAccessToken(Response response, String accessToken) {
        return response.request().newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }

    public boolean refreshToken(String refreshToken){
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        if(newAccessToken != null){
            MyApplication.getInstance().getTokenManager().clearTokens();
            MyApplication.getInstance().getTokenManager().saveTokens(newAccessToken, refreshToken);
            return true;
        }
        return false;
    }
}
