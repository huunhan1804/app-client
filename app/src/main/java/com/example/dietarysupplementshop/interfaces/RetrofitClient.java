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
    private static final String TAG = "ApiCallLog"; // Tag for logging
    private static final String BASE_URL = "https://app-server-zykr.onrender.com";

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

            // Add request logging interceptor
            httpClientBuilder.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Log.d(TAG, "API REQUEST: " + originalRequest.method() + " " + originalRequest.url());
                Log.d(TAG, "REQUEST HEADERS: " + originalRequest.headers());

                String accessToken = MyApplication.getInstance().getTokenManager().getAccessToken();

                if (accessToken != null) {
                    String url = originalRequest.url().toString();
                    if (!url.startsWith(BASE_URL + "/api/auth/")) {
                        Log.d(TAG, "Adding Authorization token to request");
                        Request request = originalRequest.newBuilder().header("Authorization", "Bearer " + accessToken).build();
                        return chain.proceed(request);
                    }
                }

                long startTime = System.currentTimeMillis();
                Response response = chain.proceed(originalRequest);
                long endTime = System.currentTimeMillis();

                Log.d(TAG, "API RESPONSE: " + response.code() + " for " + originalRequest.url());
                Log.d(TAG, "RESPONSE TIME: " + (endTime - startTime) + "ms");
                Log.d(TAG, "RESPONSE HEADERS: " + response.headers());

                return response;
            });

            // Add network response logging interceptor
            httpClientBuilder.addNetworkInterceptor(chain -> {
                Request request = chain.request();
                long startTime = System.currentTimeMillis();
                Log.d(TAG, "⬆️ Sending request: " + request.method() + " " + request.url());

                Response response = chain.proceed(request);
                long endTime = System.currentTimeMillis();

                Log.d(TAG, "⬇️ Received response for " + request.url() + " in " + (endTime - startTime) + "ms");
                Log.d(TAG, "Response status: " + response.code() + " " + response.message());

                return response;
            });

            httpClientBuilder.authenticator(tokenAuthenticator);

            OkHttpClient httpClient = httpClientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(httpClient)
                    .build();

            Log.d(TAG, "Retrofit instance created with BASE_URL: " + BASE_URL);
        }
        return retrofit;
    }
}