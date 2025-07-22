package com.example.dietarysupplementshop.responses;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.time.LocalDate;

public class AgencyInfoDTO implements Serializable {
    @SerializedName("agency_id")
    private Long agency_id;
    @SerializedName("account_id")
    private Long account_id;
    @SerializedName("agency_name")
    private String agency_name;
    @SerializedName("agency_email")
    private String agency_email;
    @SerializedName("agency_phone")
    private String agency_phone;
    @SerializedName("agency_address")
    private String agency_address;
    @SerializedName("agency_tax_code")
    private String agency_tax_code;
    @SerializedName("full_name_applicant")
    private String full_name_applicant;
    @SerializedName("birth_date_applicant")
    private LocalDate birth_date_applicant;
    @SerializedName("gender_applicant")
    private String gender_applicant;
    @SerializedName("id_card_number_applicant")
    private String id_card_number_applicant;
    @SerializedName("date_of_issue_card")
    private LocalDate date_of_issue_card;
    @SerializedName("place_of_issue_card")
    private String place_of_issue_card;
    @SerializedName("id_card_front_image_url")
    private String id_card_front_image_url;
    @SerializedName("id_card_back_image_url")
    private String id_card_back_image_url;
    @SerializedName("business_license_urls")
    private String business_license_urls;
    @SerializedName("professional_cert_urls")
    private String professional_cert_urls;
    @SerializedName("diploma_cert_urls")
    private String diploma_cert_urls;
    @SerializedName("status")
    private String status;
    @SerializedName("rejectionReason")
    private String rejectionReason;
    public AgencyInfoDTO() {}

    public Long getAgency_id() { return agency_id; }
    public void setAgency_id(Long agency_id) { this.agency_id = agency_id; }
    public Long getAccount_id() { return account_id; }
    public void setAccount_id(Long account_id) { this.account_id = account_id; }
    public String getAgency_name() { return agency_name; }
    public void setAgency_name(String agency_name) { this.agency_name = agency_name; }
    public String getAgency_email() { return agency_email; }
    public void setAgency_email(String agency_email) { this.agency_email = agency_email; }
    public String getAgency_phone() { return agency_phone; }
    public void setAgency_phone(String agency_phone) { this.agency_phone = agency_phone; }
    public String getAgency_address() { return agency_address; }
    public void setAgency_address(String agency_address) { this.agency_address = agency_address; }
    public String getAgency_tax_code() { return agency_tax_code; }
    public void setAgency_tax_code(String agency_tax_code) { this.agency_tax_code = agency_tax_code; }
    public String getFull_name_applicant() { return full_name_applicant; }
    public void setFull_name_applicant(String full_name_applicant) { this.full_name_applicant = full_name_applicant; }
    public LocalDate getBirth_date_applicant() { return birth_date_applicant; }
    public void setBirth_date_applicant(LocalDate birth_date_applicant) { this.birth_date_applicant = birth_date_applicant; }
    public String getGender_applicant() { return gender_applicant; }
    public void setGender_applicant(String gender_applicant) { this.gender_applicant = gender_applicant; }
    public String getId_card_number_applicant() { return id_card_number_applicant; }
    public void setId_card_number_applicant(String id_card_number_applicant) { this.id_card_number_applicant = id_card_number_applicant; }
    public LocalDate getDate_of_issue_card() { return date_of_issue_card; }
    public void setDate_of_issue_card(LocalDate date_of_issue_card) { this.date_of_issue_card = date_of_issue_card; }
    public String getPlace_of_issue_card() { return place_of_issue_card; }
    public void setPlace_of_issue_card(String place_of_issue_card) { this.place_of_issue_card = place_of_issue_card; }
    public String getId_card_front_image_url() { return id_card_front_image_url; }
    public void setId_card_front_image_url(String id_card_front_image_url) { this.id_card_front_image_url = id_card_front_image_url; }
    public String getId_card_back_image_url() { return id_card_back_image_url; }
    public void setId_card_back_image_url(String id_card_back_image_url) { this.id_card_back_image_url = id_card_back_image_url; }
    public String getBusiness_license_urls() { return business_license_urls; }
    public void setBusiness_license_urls(String business_license_urls) { this.business_license_urls = business_license_urls; }
    public String getProfessional_cert_urls() { return professional_cert_urls; }
    public void setProfessional_cert_urls(String professional_cert_urls) { this.professional_cert_urls = professional_cert_urls; }
    public String getDiploma_cert_urls() { return diploma_cert_urls; }
    public void setDiploma_cert_urls(String diploma_cert_urls) { this.diploma_cert_urls = diploma_cert_urls; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}