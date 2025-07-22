package com.example.dietarysupplementshop.requests;

public class UpdateStatusRequest {
    private String newStatus;

    public UpdateStatusRequest() {
    }

    public UpdateStatusRequest(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}