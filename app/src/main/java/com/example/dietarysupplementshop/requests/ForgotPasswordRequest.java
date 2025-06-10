package com.example.dietarysupplementshop.requests;

public class ForgotPasswordRequest {
    private String loginId;
    private String new_password;

    private String otp_code;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String loginId, String new_password, String otp_code) {
        this.loginId = loginId;
        this.new_password = new_password;
        this.otp_code = otp_code;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }
}
