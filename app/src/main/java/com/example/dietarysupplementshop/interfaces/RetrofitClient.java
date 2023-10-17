package com.example.dietarysupplementshop.interfaces;

import android.content.Intent;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;
import com.example.dietarysupplementshop.services.AuthService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static AuthService authService = new AuthService(MyApplication.getInstance().getTokenManager());
    private static final String BASE_URL = "https://app-shoppingsystem-main-231014093450.azurewebsites.net";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

            // Thêm logging interceptor cho việc ghi log HTTP request/response
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            httpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
            httpClientBuilder.retryOnConnectionFailure(true);
            httpClientBuilder.addInterceptor(loggingInterceptor);

            // Thêm interceptor cho việc gửi mã token trong header
            httpClientBuilder.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                String accessToken = MyApplication.getInstance().getTokenManager().getAccessToken();

                if (accessToken != null && !originalRequest.url().toString().endsWith("/api/auth/refresh-token")) {
                    Request request = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + accessToken)
                            .build();
                    return chain.proceed(request);
                }

                return chain.proceed(originalRequest);
            });

            // Thêm interceptor cho xử lý khi response trả về mã lỗi 401 hoặc 403
            httpClientBuilder.addInterceptor(chain -> {
                Request request = chain.request();
                Response response = chain.proceed(request);
                Log.d("Mytag",  response.code() + "");

                if (response.code() == 401 || response.code() == 403) {
                    synchronized (httpClientBuilder) {
                        String refreshToken = MyApplication.getInstance().getTokenManager().getRefreshToken();

                        if (refreshToken != null) {
                            authService.refreshAccessToken(refreshToken, new AuthService.AuthCallback() {
                                @Override
                                public void onSuccess(AuthenticateResponse response) throws IOException {
                                    Log.d("Mytag", "Diu kha");
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    Log.d("Mytag", "Xu ca nma");
                                    MyApplication.getInstance().getTokenManager().clearTokens();
                                    Intent logoutIntent = new Intent("com.example.dietarysupplementshop.ACTION_LOGOUT");
                                    MyApplication.getInstance().getApplicationContext().sendBroadcast(logoutIntent);
                                }
                            });
                        }
                    }
                }

                return response;
            });

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
