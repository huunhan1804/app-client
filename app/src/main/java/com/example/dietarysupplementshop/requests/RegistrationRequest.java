package com.example.dietarysupplementshop.requests;

public class RegistrationRequest {
    private String loginId;
    private String password;
    private String fullname;
    private String otp_code;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String loginId, String password, String fullname, String otp_code) {
        this.loginId = loginId;
        this.password = password;
        this.fullname = fullname;
        this.otp_code = otp_code;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }
}
