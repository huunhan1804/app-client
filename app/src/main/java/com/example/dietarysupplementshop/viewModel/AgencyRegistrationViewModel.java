package com.example.dietarysupplementshop.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.model.AgencyRegistrationData;
import com.example.dietarysupplementshop.repositories.IAgencyRepository;
import com.example.dietarysupplementshop.repositories.RepositoryProvider;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.AgencyRegistrationResponse;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import com.example.dietarysupplementshop.requests.AgencyRegistrationRequest;

public class AgencyRegistrationViewModel extends AndroidViewModel {
    private static final String TAG = "AgencyRegVM";
    private final IAgencyRepository repository;
    public AgencyRegistrationViewModel(@NonNull Application application) {
        super(application);
        this.repository = RepositoryProvider.getAgencyRepository();
    }
    public LiveData<Resource<AgencyRegistrationResponse>> getAgencyRegistrationStatus(long accountId) {
        return repository.getAgencyRegistrationStatus(accountId);
    }
    public LiveData<Resource<AgencyInfoDTO>> submitRegistration(AgencyRegistrationData data) {
        Gson gson = new Gson();
        AgencyRegistrationRequest requestBackend = new AgencyRegistrationRequest(
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

        return repository.registerAgency(requestBody);
    }
}