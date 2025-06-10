package com.example.dietarysupplementshop.requests;

import java.util.Date;

public class UpdateAccountRequest {
    private String fullname;
    private String gender;
    private String birthday;

    public UpdateAccountRequest() {
    }

    public UpdateAccountRequest(String fullname, String gender, String birthday) {
        this.fullname = fullname;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
