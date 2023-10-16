package com.example.dietarysupplementshop.token;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;
import com.example.dietarysupplementshop.services.AuthService;
import java.io.IOException;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private Context context;

    private TokenManager tokenManager;
    private AuthService authService;
    private final Object lock = new Object();

    public TokenAuthenticator(Context context, TokenManager tokenManager, AuthService authService) {
        this.context = context;
        this.tokenManager = tokenManager;
        this.authService = authService;
    }


    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        synchronized (lock) {
            String currentToken = tokenManager.getAccessToken();

            if (currentToken != null && currentToken.equals(response.request().header("Authorization"))) {
                String refreshToken = tokenManager.getRefreshToken();

                if (refreshToken == null) {
                    return null;
                }

                retrofit2.Response<ResponseModel<AuthenticateResponse>> refreshResponse = authService.refreshAccessTokenSync(refreshToken);

                if (refreshResponse.isSuccessful()) {
                    ResponseModel<AuthenticateResponse> responseModel = refreshResponse.body();
                    if (responseModel != null && responseModel.getStatus() == 200) {
                        AuthenticateResponse authenticateResponse = responseModel.getData();
                        tokenManager.saveTokens(authenticateResponse.getAccess_token(), authenticateResponse.getRefresh_token());
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + authenticateResponse.getAccess_token())
                                .build();
                    } else {
                        tokenManager.clearTokens();
                        Intent logoutIntent = new Intent("com.example.dietarysupplementshop.ACTION_LOGOUT");
                        context.sendBroadcast(logoutIntent);
                    }
                } else {
                    tokenManager.clearTokens();
                    Intent logoutIntent = new Intent("com.example.dietarysupplementshop.ACTION_LOGOUT");
                    context.sendBroadcast(logoutIntent);
                }
            }

            return null;
        }
    }

}
