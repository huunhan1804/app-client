package com.example.dietarysupplementshop.requests;

import com.example.dietarysupplementshop.model.ShopRegistrationData;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SellerRegistrationRequestForBackend implements Serializable {

    @SerializedName("registrationData")
    public ShopRegistrationData registrationData;
    @SerializedName("idCardFrontUrl")
    public String idCardFrontUrl;
    @SerializedName("idCardBackUrl")
    public String idCardBackUrl;
    @SerializedName("businessLicenseUrl")
    public String businessLicenseUrl;
    @SerializedName("professionalCertificateUrl")
    public String professionalCertificateUrl;
    @SerializedName("diplomaCertificateUrl")
    public String diplomaCertificateUrl;

    public SellerRegistrationRequestForBackend(ShopRegistrationData registrationData, String idCardFrontUrl, String idCardBackUrl, String businessLicenseUrl, String professionalCertificateUrl, String diplomaCertificateUrl) {
        this.registrationData = registrationData;
        this.idCardFrontUrl = idCardFrontUrl;
        this.idCardBackUrl = idCardBackUrl;
        this.businessLicenseUrl = businessLicenseUrl;
        this.professionalCertificateUrl = professionalCertificateUrl;
        this.diplomaCertificateUrl = diplomaCertificateUrl;
    }
}