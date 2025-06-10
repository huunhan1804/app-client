package com.example.dietarysupplementshop.requests;

public class AddLoginIdRequest {
    private String loginId;

    public AddLoginIdRequest(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
