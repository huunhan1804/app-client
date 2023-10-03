package com.example.dietarysupplementshop.network;

import com.example.dietarysupplementshop.requests.LoginRequest;
import com.example.dietarysupplementshop.requests.RefreshTokenRequest;
import com.example.dietarysupplementshop.responses.LoginResponse;
import com.example.dietarysupplementshop.responses.RefreshTokenResponse;
import com.example.dietarysupplementshop.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginManager {

    private static final String BASE_URL = "https://api2-java05.azurewebsites.net/";
    private static LoginManager instance;
    private ApiService apiService;

    private LoginManager() {
        apiService = RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public void login(String email, String password, Callback<LoginResponse> callback) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(callback);
    }

    public void refreshAccessToken(String refreshToken, Callback<RefreshTokenResponse> callback) {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
        Call<RefreshTokenResponse> call = apiService.refreshAccessToken(refreshTokenRequest);
        call.enqueue(callback);
    }

}

