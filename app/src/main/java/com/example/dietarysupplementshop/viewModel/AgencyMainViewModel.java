package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.repositories.AgencyProductRepository;
import com.example.dietarysupplementshop.responses.ShopInfoDTO;

public class AgencyMainViewModel extends ViewModel {
    private final AgencyProductRepository agencyProductRepository;
    private final MutableLiveData<ShopInfoDTO> _shopInfo = new MutableLiveData<>();
    public LiveData<ShopInfoDTO> shopInfo = _shopInfo;
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    public AgencyMainViewModel() {
        agencyProductRepository = new AgencyProductRepository();
    }


    public void loadShopInfo() {
        agencyProductRepository.getShopInfo(new AgencyProductRepository.ApiCallback<ShopInfoDTO>() {
            @Override
            public void onSuccess(ShopInfoDTO result) {
                _shopInfo.postValue(result);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
            }
        });
    }
}