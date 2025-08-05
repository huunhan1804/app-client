package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.AgencyRegistrationResponse;
import okhttp3.RequestBody; // Chỉ cần RequestBody

public interface IAgencyRepository {
    LiveData<Resource<AgencyInfoDTO>> registerAgency(RequestBody requestBodyJson);
    LiveData<Resource<AgencyRegistrationResponse>> getAgencyRegistrationStatus(long accountId);
}