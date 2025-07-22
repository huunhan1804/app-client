package com.example.dietarysupplementshop.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.model.ShopRegistrationData;
import com.example.dietarysupplementshop.repositories.ISellerRepository;
import com.example.dietarysupplementshop.repositories.RepositoryProvider;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import com.example.dietarysupplementshop.requests.SellerRegistrationRequestForBackend;

public class SellerRegistrationViewModel extends AndroidViewModel {
    private static final String TAG = "SellerRegVM";
    private final ISellerRepository repository;
    public SellerRegistrationViewModel(@NonNull Application application) {
        super(application);
        this.repository = RepositoryProvider.getSellerRepository();
    }
    public LiveData<Resource<SellerRegistrationResponse>> getSellerRegistrationStatus(long accountId) {
        return repository.getSellerRegistrationStatus(accountId);
    }
    public LiveData<Resource<AgencyInfoDTO>> submitRegistration(ShopRegistrationData data) {
        Gson gson = new Gson();
        SellerRegistrationRequestForBackend requestBackend = new SellerRegistrationRequestForBackend(
                data,
                data.getIdCardFrontUrl(),
                data.getIdCardBackUrl(),
                data.getBusinessLicenseUrl(),
                data.getProfessionalCertificateUrl(),
                data.getDiplomaCertificateUrl()
        );
        String requestJson = gson.toJson(requestBackend);
        Log.d(TAG, "Registration Data JSON sent to backend: " + requestJson);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestJson);

        return repository.registerSeller(requestBody);
    }
}