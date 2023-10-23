package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.repositories.AccountRepository;
import com.example.dietarysupplementshop.requests.AddAddressRquest;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.UpdateAddressRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.repositories.Resource;

import java.util.List;

public class AccountViewModel extends ViewModel {
    private final AccountRepository accountRepository;

    private MutableLiveData<Resource<AccountInformation>> accountInfoResource;

    private MutableLiveData<Resource<List<Address>>> addressListResource;

    private String avatar_url;

    public String getAvatar_url() {
        return avatar_url;
    }

    public AccountViewModel() {
        this.accountRepository = new AccountRepository();
    }

    public LiveData<Resource<AccountInformation>> getAccountInfoResource() {
        if (accountInfoResource == null) {
            accountInfoResource = new MutableLiveData<>();
            loadAccountInfo();
        }
        return accountInfoResource;
    }

    private void loadAccountInfo() {
        accountRepository.fetchAccount().observeForever(accountInfoResource::setValue);
    }

    public void updateAvatar(String avatarUrl) {
        accountRepository.fetchUpdateAvatar(avatarUrl).observeForever(updatedAccountInfo -> {
            accountInfoResource.setValue(updatedAccountInfo);
            if (updatedAccountInfo.getStatus() == Resource.Status.SUCCESS && updatedAccountInfo.getData() != null) {
                avatar_url = updatedAccountInfo.getData().getAvatar_url();
            }
        });
    }

    public LiveData<Resource<AccountInformation>> addToCart(AddToCartRequest request) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        accountRepository.fetchAddToCart(request).observeForever(accountInformationResource -> {
            data.setValue(accountInformationResource);
            if (accountInformationResource != null) {
                accountInfoResource.setValue(accountInformationResource);
            }
        });
        return data;
    }



    public void increaseCartItemQuantity(AddToCartRequest request) {
        accountRepository.fetchIncreaseCartItemQuantity(request).observeForever(accountInfoResource::setValue);
    }
    public void decreaseCartItemQuantity(AddToCartRequest request) {
        accountRepository.fetchDecreaseCartItemQuantity(request).observeForever(accountInfoResource::setValue);
    }
    public void deleteCartItem(long cart_item_id) {
        accountRepository.fetchDeleteCartItem(cart_item_id).observeForever(accountInfoResource::setValue);
    }


    public LiveData<Resource<List<Address>>> getAddressListResource() {
        if (addressListResource == null) {
            addressListResource = new MutableLiveData<>();
            loadAddressList();
        }
        return addressListResource;
    }

    private void loadAddressList() {
        accountRepository.fetchAddressList().observeForever(addressListResource::setValue);
    }

    public LiveData<Resource<Address>> getInfoAddress(long addressId) {
        return accountRepository.fetchInfoAddress(addressId);
    }

    public void setDefaultAddress(Long addressId) {
        accountRepository.fetchSetDefaultAddress(addressId).observeForever(addresses -> addressListResource.setValue(addresses));
    }

    public void addAddress(String fullname, String phone, String address_detail) {
        AddAddressRquest request = new AddAddressRquest(fullname, phone, address_detail);
        accountRepository.addAddress(request).observeForever(addresses -> {
            if (addresses != null) {
                if (addressListResource == null) {
                    addressListResource = new MutableLiveData<>();
                }
                addressListResource.setValue(addresses);
            }
        });
    }

    public void updateAddress(UpdateAddressRequest request) {
        accountRepository.updateAddress(request).observeForever(addresses -> {
            if (addresses != null) {
                if (addressListResource == null) {
                    addressListResource = new MutableLiveData<>();
                }
                addressListResource.setValue(addresses);
            }
        });
    }
}
