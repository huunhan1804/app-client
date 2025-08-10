package com.example.dietarysupplementshop.requests;

import java.io.Serializable;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AddFeedbackRequest implements Serializable {

    @SerializedName("productId")
    private long productId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("comment")
    private String comment;

    public AddFeedbackRequest(long productId, int rating, String comment) {
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}