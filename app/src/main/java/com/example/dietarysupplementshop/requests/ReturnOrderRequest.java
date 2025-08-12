package com.example.dietarysupplementshop.requests;

public class ReturnOrderRequest {
    private long orderId;
    private String returnReason;

    public ReturnOrderRequest(long orderId, String returnReason) {
        this.orderId = orderId;
        this.returnReason = returnReason;
        }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
}
