package com.example.dietarysupplementshop.responses;
public class SellerRegistrationResponse {
    private Long applicationId;
    private String status;
    private String message;
    private String rejectionReason;
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
