package com.example.dietarysupplementshop.requests;

public class UpdateAvatarRequest {
    private String avatar_url;

    public UpdateAvatarRequest() {
    }

    public UpdateAvatarRequest(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
