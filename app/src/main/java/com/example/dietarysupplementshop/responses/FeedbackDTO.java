package com.example.dietarysupplementshop.responses;

public class FeedbackDTO {
    private String user_name;
    private double rating;
    private String comment;

    public FeedbackDTO(String user_name, double rating, String comment) {
        this.user_name = user_name;
        this.rating = rating;
        this.comment = comment;
    }

    public FeedbackDTO() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
