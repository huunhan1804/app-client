package com.example.dietarysupplementshop;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.token.TokenManager;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push_notification_id";
    private static MyApplication instance;
    private TokenManager tokenManager;

    private AccountViewModel accountViewModel;
    private ProductViewModel productViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        tokenManager = new TokenManager(getApplicationContext());
        accountViewModel = new AccountViewModel();
        productViewModel = new ProductViewModel();
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

    public void sendLogoutBroadcast() {
        Intent logoutIntent = new Intent("com.example.dietarysupplementshop.ACTION_LOGOUT");
        sendBroadcast(logoutIntent);
    }
    public AccountViewModel getAccountViewModel() {
        return accountViewModel;
    }

    public ProductViewModel getProductViewModel() {
        return productViewModel;
    }
}
