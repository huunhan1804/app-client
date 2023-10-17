package com.example.dietarysupplementshop.responses;

public class AccountProfile {
    private String fullname;
    private String email;
    private String phone;
    private String gender;
    private String birthday;

    public AccountProfile() {
    }

    public AccountProfile(String fullname, String email, String phone, String gender, String birthday) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
