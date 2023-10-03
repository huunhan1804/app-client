package com.example.dietarysupplementshop.services;

import com.example.dietarysupplementshop.requests.LoginRequest;
import com.example.dietarysupplementshop.requests.RefreshTokenRequest;
import com.example.dietarysupplementshop.responses.LoginResponse;
import com.example.dietarysupplementshop.responses.RefreshTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/v1/auth/authenticate")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("/api/v1/auth/refreshToken")
    Call<RefreshTokenResponse> refreshAccessToken(@Body RefreshTokenRequest refreshTokenRequest);
}
