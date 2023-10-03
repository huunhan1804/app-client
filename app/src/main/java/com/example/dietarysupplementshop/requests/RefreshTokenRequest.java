package com.example.dietarysupplementshop.requests;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequest {
    @SerializedName("token")
    private String token;
    public RefreshTokenRequest(String token) {
        this.token = token;
    }
}
