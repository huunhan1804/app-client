package com.example.dietarysupplementshop.requests;

public class AuthenticateRequest {
    private String loginId;
    private String password;

    public AuthenticateRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public AuthenticateRequest() {
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
