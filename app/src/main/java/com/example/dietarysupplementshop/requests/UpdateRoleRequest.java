package com.example.dietarysupplementshop.requests;

public class UpdateRoleRequest {
    private long account_id;
    private String role_code;

    public UpdateRoleRequest(long account_id, String role_code) {
        this.account_id = account_id;
        this.role_code = role_code;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }
}