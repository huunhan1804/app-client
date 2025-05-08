package com.example.dietarysupplementshop.responses;

public class AccountInformation {
    private long id;
    private String avatar_url;
    private String role_code;
    private CartInformation cart_info;
    private AccountProfile accountProfileDTO;

    public AccountInformation() {
    }

    public AccountInformation(long id, String avatar_url, String role_code, CartInformation cart_info, AccountProfile accountProfileDTO) {
        this.id = id;
        this.avatar_url = avatar_url;
        this.role_code = role_code;
        this.cart_info = cart_info;
        this.accountProfileDTO = accountProfileDTO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }

    public CartInformation getCart_info() {
        return cart_info;
    }

    public void setCart_info(CartInformation cart_info) {
        this.cart_info = cart_info;
    }

    public AccountProfile getAccountProfileDTO() {
        return accountProfileDTO;
    }

    public void setAccountProfileDTO(AccountProfile accountProfileDTO) {
        this.accountProfileDTO = accountProfileDTO;
    }
}
