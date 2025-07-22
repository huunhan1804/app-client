package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;
import okhttp3.RequestBody; // Chỉ cần RequestBody

public interface ISellerRepository {
    LiveData<Resource<AgencyInfoDTO>> registerSeller(RequestBody requestBodyJson);
    LiveData<Resource<SellerRegistrationResponse>> getSellerRegistrationStatus(long accountId);
}