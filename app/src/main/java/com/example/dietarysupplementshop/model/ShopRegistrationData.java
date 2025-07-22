package com.example.dietarysupplementshop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopRegistrationData implements Serializable {

    @SerializedName("shopName")
    private String shopName;
    @SerializedName("address")
    private String address;
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("taxNumber")
    private String taxNumber;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("gender")
    private String gender;
    @SerializedName("idCardNumber")
    private String idCardNumber;
    @SerializedName("dateOfIssue")
    private String dateOfIssue;
    @SerializedName("placeOfIssue")
    private String placeOfIssue;
    @SerializedName("idCardFrontUrl")
    private String idCardFrontUrl;
    @SerializedName("idCardBackUrl")
    private String idCardBackUrl;
    @SerializedName("businessLicenseUrl")
    private String businessLicenseUrl;
    @SerializedName("professionalCertificateUrl")
    private String professionalCertificateUrl;
    @SerializedName("diplomaCertificateUrl")
    private String diplomaCertificateUrl;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }

    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }

    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public String getProfessionalCertificateUrl() {
        return professionalCertificateUrl;
    }

    public void setProfessionalCertificateUrl(String professionalCertificateUrl) {
        this.professionalCertificateUrl = professionalCertificateUrl;
    }

    public String getDiplomaCertificateUrl() {
        return diplomaCertificateUrl;
    }

    public void setDiplomaCertificateUrl(String diplomaCertificateUrl) {
        this.diplomaCertificateUrl = diplomaCertificateUrl;
    }

}