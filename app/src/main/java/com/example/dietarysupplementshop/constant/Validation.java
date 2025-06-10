package com.example.dietarysupplementshop.constant;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Validation {
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        return email.matches(emailPattern);
    }

    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }

    public static boolean isValidEmailOrPhone(String input) {
        return isValidEmail(input) || isValidPhoneNumber(input);
    }
    public static boolean isValidUsernameOrEmailOrPhone(String input) {
        return isValidEmail(input) || isValidPhoneNumber(input) || isValidUsername(input);
    }


    public static boolean isValidPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isValidOTP(String otp) {
        String otpPattern = "^[0-9]{6}$";
        return otp.matches(otpPattern);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^[0-9]{10}$";
        return phoneNumber.matches(phonePattern);
    }

    public static boolean isValidUsername(String username) {
        String usernamePattern = "^[a-zA-Z0-9]{6,20}$";
        return username.matches(usernamePattern);
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static String formatPriceToVND(double price) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return numberFormat.format(price);
    }
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(date);
    }



}
