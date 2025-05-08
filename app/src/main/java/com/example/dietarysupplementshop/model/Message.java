package com.example.dietarysupplementshop.model;

public class Message {
    private String text;
    private boolean isSent; // true if the message is sent by the user, false if received

    public Message(String text, boolean isSent) {
        this.text = text;
        this.isSent = isSent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}

