package com.example.dietarysupplementshop.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    String smsMessage = "";
                    for (Object pdu : pdus) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                        smsMessage += sms.getMessageBody();
                    }

                    // Xác định và trích xuất OTP từ smsMessage
                    String otp = extractOTP(smsMessage);

                    // Đưa OTP vào EditText hoặc xử lý theo ý muốn
                    Log.d("SMS OTP", "Received OTP: " + otp);
                }
            }
        }
    }

    // Hàm trích xuất OTP từ tin nhắn SMS
    private String extractOTP(String message) {
        // Thực hiện xác định và trích xuất OTP từ tin nhắn SMS
        // Đây là một ví dụ đơn giản, cần xem xét định dạng cụ thể của OTP
        return message.replaceAll("[^0-9]", "");
    }
}
