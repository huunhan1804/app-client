package com.example.dietarysupplementshop;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.dietarysupplementshop.token.TokenManager;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push_notification_id";
    private static MyApplication instance;
    private TokenManager tokenManager;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        tokenManager = new TokenManager(getApplicationContext());
        createChannelNotification();
    }

    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    public static MyApplication getInstance() {
        return instance;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

}
