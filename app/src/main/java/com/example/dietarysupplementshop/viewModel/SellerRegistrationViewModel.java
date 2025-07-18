package com.example.dietarysupplementshop.viewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.model.ShopRegistrationData;
import com.example.dietarysupplementshop.repositories.ISellerRepository;
import com.example.dietarysupplementshop.repositories.RepositoryProvider;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;
import com.example.dietarysupplementshop.util.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SellerRegistrationViewModel extends AndroidViewModel {

    private final ISellerRepository repository;

    public SellerRegistrationViewModel(@NonNull Application application) {
        super(application);
        this.repository = RepositoryProvider.getSellerRepository();
    }

    public LiveData<Resource<SellerRegistrationResponse>> getSellerRegistrationStatus(long accountId) {
        return repository.getSellerRegistrationStatus(accountId);
    }

    public LiveData<Resource<SellerRegistrationResponse>> submitRegistration(ShopRegistrationData data) {
        Gson gson = new Gson();
        data.setIdFrontImageUri(null);
        data.setIdBackImageUri(null);
        data.setBusinessLicenseUris(null);
        data.setProfessionalCertificatesUris(null);
        data.setDiplomaCertificatesUris(null);
        String shopDataJson = gson.toJson(data);
        RequestBody shopDataRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), shopDataJson);

        MultipartBody.Part idCardFrontPart = createPartFromUri(data.getIdFrontImageUri(), "idCardFront");
        MultipartBody.Part idCardBackPart = createPartFromUri(data.getIdBackImageUri(), "idCardBack");

        List<MultipartBody.Part> businessLicenseParts = createPartsFromUriList(data.getBusinessLicenseUris(), "businessLicense");
        List<MultipartBody.Part> professionalCertificatesParts = createPartsFromUriList(data.getProfessionalCertificatesUris(), "professionalCertificates");
        List<MultipartBody.Part> diplomaCertificatesParts = createPartsFromUriList(data.getDiplomaCertificatesUris(), "diplomaCertificates");
        return repository.registerSeller(
                shopDataRequestBody,
                idCardFrontPart,
                idCardBackPart,
                businessLicenseParts,
                professionalCertificatesParts,
                diplomaCertificatesParts
        );
    }

    private MultipartBody.Part createPartFromUri(String uriString, String partName) {
        if (uriString != null && !uriString.isEmpty()) {
            Uri uri = Uri.parse(uriString);
            File file = FileUtils.getFileFromUri(getApplication().getApplicationContext(), uri);
            if (file != null) {
                String mimeType = getApplication().getContentResolver().getType(uri);
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType != null ? mimeType : "application/octet-stream"), file);
                return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
            }
        }
        return null;
    }

    private List<MultipartBody.Part> createPartsFromUriList(List<String> uriStrings, String partName) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (uriStrings != null) {
            for (String uriString : uriStrings) {
                MultipartBody.Part part = createPartFromUri(uriString, partName + "[]");
                if (part != null) {
                    parts.add(part);
                }
            }
        }
        return parts;
    }
}
