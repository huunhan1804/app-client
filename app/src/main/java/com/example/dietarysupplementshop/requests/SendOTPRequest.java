package com.example.dietarysupplementshop.requests;

public class SendOTPRequest {
    private String loginId;

    public SendOTPRequest(String loginId) {
        this.loginId = loginId;
    }

    public SendOTPRequest() {
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
