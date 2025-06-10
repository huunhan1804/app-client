package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.token.TokenManager;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;
import com.example.dietarysupplementshop.viewModel.SearchHistoryViewModel;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push_notification_id";
    private static MyApplication instance;
    private TokenManager tokenManager;
    private SearchHistoryViewModel searchHistoryViewModel;

    private AccountViewModel accountViewModel;
    private ProductViewModel productViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        tokenManager = new TokenManager(getApplicationContext());
        accountViewModel = new AccountViewModel();
        productViewModel = new ProductViewModel();
        searchHistoryViewModel = new SearchHistoryViewModel(this);
        createChannelNotification();
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String strTitle, String strMessage) {
        //
        Intent intent = new Intent(this, HomepageActivity.class);
        intent.putExtra("navigateTo", "CartFragment");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(strTitle)
                .setContentText(strMessage)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);
        Notification notification = noBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(1, notification);
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

    public SearchHistoryViewModel getSearchHistoryViewModel() {
        return searchHistoryViewModel;
    }
}
