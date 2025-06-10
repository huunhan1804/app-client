package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.repositories.AccountRepository;
import com.example.dietarysupplementshop.requests.AddAddressRquest;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.ChangePasswordRequest;
import com.example.dietarysupplementshop.requests.CheckoutRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.requests.UpdateAccountRequest;
import com.example.dietarysupplementshop.requests.UpdateAddressRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;

import java.util.List;

public class AccountViewModel extends ViewModel {
    private final AccountRepository accountRepository;

    private MutableLiveData<Resource<AccountInformation>> accountInfoResource;

    private MutableLiveData<Resource<List<Address>>> addressListResource;
    private MutableLiveData<Resource<List<Order>>> orderListResource;

    private String avatar_url;

    public String getAvatar_url() {
        if(avatar_url == null){
            loadAccountInfo();
        }
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

    public void loadAccountInfo() {
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

    public LiveData<Resource<String>> changePassword(ChangePasswordRequest changePasswordRequest) {
        return accountRepository.changePassword(changePasswordRequest);
    }



    public LiveData<Resource<AccountInformation>> updateAccountProfile(UpdateAccountRequest request) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        accountRepository.updateAccountProfile(request).observeForever(accountInformationResource -> {
            data.setValue(accountInformationResource);
            if (accountInformationResource != null) {
                accountInfoResource.setValue(accountInformationResource);
            }
        });
        return data;
    }

    public LiveData<Resource<AccountInformation>> addLoginId(String loginId) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        accountRepository.fetchAddLoginId(loginId).observeForever(accountInformationResource -> {
            data.setValue(accountInformationResource);
            if (accountInformationResource != null) {
                accountInfoResource.setValue(accountInformationResource);
            }
        });
        return data;
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

    public LiveData<Resource<List<Order>>> getOrderListResource() {
        if (orderListResource == null) {
            orderListResource = new MutableLiveData<>();
            loadOrderList();
        }
        return orderListResource;
    }

    public void loadOrderList() {
        accountRepository.fetchOrderList().observeForever(orderListResource::setValue);
    }
    public void cancelOrder(Long orderId) {
        accountRepository.cancelOrder(orderId).observeForever(orderListResource::setValue);
    }

    public LiveData<Resource<Order>> getOrderInfo(long orderId) {
        return accountRepository.getOrderInfo(orderId);
    }

    public LiveData<Resource<Order>> addOrder(OrderRequest orderRequest) {
        return accountRepository.addOrder(orderRequest);
    }

    public LiveData<Resource<List<OrderDetailResponse>>> getOrderDetailCheckout(CheckoutRequest request) {
        return accountRepository.getOrderDetailCheckout(request);
    }

    public void reloadAccountInfo() {
        accountRepository.reloadAccountInfo();
        loadAccountInfo();
    }
    public void reloadAddressList() {
        accountRepository.reloadAddressList();
        loadAddressList();
    }
    public void reloadOrderList() {
        accountRepository.reloadOrderList();
        loadOrderList();
    }
}
