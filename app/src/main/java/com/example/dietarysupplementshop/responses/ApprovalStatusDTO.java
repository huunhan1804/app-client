package com.example.dietarysupplementshop.responses;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ApprovalStatusDTO implements Serializable {
    @SerializedName("statusId")
    private int statusId;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusName")
    private String statusName;

    // Getters and Setters
    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}