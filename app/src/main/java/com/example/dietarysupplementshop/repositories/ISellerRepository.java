package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface ISellerRepository {
    LiveData<Resource<SellerRegistrationResponse>> registerSeller(
            RequestBody shopDataJson,
            MultipartBody.Part idCardFront,
            MultipartBody.Part idCardBack,
            List<MultipartBody.Part> businessLicense,
            List<MultipartBody.Part> professionalCertificates,
            List<MultipartBody.Part> diplomaCertificates
    );

    LiveData<Resource<SellerRegistrationResponse>> getSellerRegistrationStatus(long accountId);
}