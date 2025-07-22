package com.example.dietarysupplementshop.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    public enum Status {SUCCESS, ERROR, LOADING}

    private Status status;
    private T data;
    private String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message, @Nullable Integer code) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    @Nullable
    public final Integer code;

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data, @Nullable Integer code) {
        return new Resource<>(Status.ERROR, data, msg, code);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null, null);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

