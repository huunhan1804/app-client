package com.example.dietarysupplementshop.interfaces;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.dietarysupplementshop.HomepageActivity;
import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.SignInActivity;
import com.example.dietarysupplementshop.SplashActivity;
import com.example.dietarysupplementshop.services.AuthService;
import com.example.dietarysupplementshop.token.TokenAuthenticator;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static final String BASE_URL = "http://192.168.1.15:8080";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            TokenAuthenticator tokenAuthenticator = new TokenAuthenticator();
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
            httpClientBuilder.retryOnConnectionFailure(true);
            httpClientBuilder.addInterceptor(loggingInterceptor);

            httpClientBuilder.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                String accessToken = MyApplication.getInstance().getTokenManager().getAccessToken();

                if (accessToken != null) {
                    String url = originalRequest.url().toString();
                    if (!url.startsWith(BASE_URL + "/api/auth/")) {
                        Log.d("Mytag", "API cần token");
                        Request request = originalRequest.newBuilder().header("Authorization", "Bearer " + accessToken).build();
                        return chain.proceed(request);
                    }
                }

                return chain.proceed(originalRequest);
            });

            httpClientBuilder.authenticator(tokenAuthenticator);

            OkHttpClient httpClient = httpClientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}
