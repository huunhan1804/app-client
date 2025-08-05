package com.example.dietarysupplementshop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApprovalStatus implements Serializable {
    @SerializedName("status_id")
    private int statusId;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("status_name")
    private String statusName;

    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}