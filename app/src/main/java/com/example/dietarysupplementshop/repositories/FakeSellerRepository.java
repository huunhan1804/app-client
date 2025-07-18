package com.example.dietarysupplementshop.repositories;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FakeSellerRepository implements ISellerRepository {

    private String currentStatus = "not_registered";
    private String rejectionReason = null;
    private static FakeSellerRepository instance;

    public static FakeSellerRepository getInstance() {
        if (instance == null) {
            instance = new FakeSellerRepository();
        }
        return instance;
    }

    @Override
    public LiveData<Resource<SellerRegistrationResponse>> registerSeller(
            RequestBody shopDataJson,
            MultipartBody.Part idCardFront,
            MultipartBody.Part idCardBack,
            List<MultipartBody.Part> businessLicense,
            List<MultipartBody.Part> professionalCertificates,
            List<MultipartBody.Part> diplomaCertificates) {

        MutableLiveData<Resource<SellerRegistrationResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            this.currentStatus = "pending";
            SellerRegistrationResponse response = new SellerRegistrationResponse();
            response.setStatus("pending");
            response.setMessage("Hồ sơ đã được gửi đi và đang chờ xét duyệt.");
            result.setValue(Resource.success(response));
        }, 2000);

        return result;
    }

    @Override
    public LiveData<Resource<SellerRegistrationResponse>> getSellerRegistrationStatus(long accountId) {
        MutableLiveData<Resource<SellerRegistrationResponse>> data = new MutableLiveData<>();
        SellerRegistrationResponse response = new SellerRegistrationResponse();
        response.setStatus(currentStatus);
        response.setRejectionReason(rejectionReason);
        data.setValue(Resource.success(response));
        return data;
    }

    public void setFakeStatus(String status, String reason) {
        this.currentStatus = status;
        this.rejectionReason = reason;
    }

}