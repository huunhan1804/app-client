package com.example.dietarysupplementshop.requests;

public class AuthenticateSocialRequest {
    private String token;

    public AuthenticateSocialRequest(String token) {
        this.token = token;
    }

    public AuthenticateSocialRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
