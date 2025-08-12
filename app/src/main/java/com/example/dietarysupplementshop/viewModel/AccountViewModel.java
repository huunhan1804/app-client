package com.example.dietarysupplementshop.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;

public class AccountViewModel extends ViewModel {


    private final AccountRepository accountRepository;
    private final MediatorLiveData<Resource<AccountInformation>> accountInfoResource;

    public AccountViewModel() {
        this.accountRepository = AccountRepository.getInstance();
        this.accountInfoResource = new MediatorLiveData<>();
        loadAccountInfo();
    }

    public LiveData<Resource<AccountInformation>> getAccountInfo() {
        return accountInfoResource;
    }

    public void loadAccountInfo() {
        accountInfoResource.setValue(Resource.loading(null));
        final LiveData<Resource<AccountInformation>> source = accountRepository.fetchAccount();
        accountInfoResource.addSource(source, resource -> {
            accountInfoResource.setValue(resource);
            if (resource.getStatus() != Resource.Status.LOADING) {
                accountInfoResource.removeSource(source);
            }
        });
    }

    public void updateAvatar(Uri imageUri) {
        accountInfoResource.setValue(Resource.loading(accountInfoResource.getValue() != null ? accountInfoResource.getValue().getData() : null));

        Runnable uploadAction = () -> {
            String fileName = "avatar_" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("avatars/" + fileName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        final LiveData<Resource<AccountInformation>> source = accountRepository.fetchUpdateAvatar(uri.toString());
                        accountInfoResource.addSource(source, resource -> {
                            accountInfoResource.setValue(resource);
                            if (resource.getStatus() != Resource.Status.LOADING) {
                                accountInfoResource.removeSource(source);
                            }
                        });
                    }))
                    .addOnFailureListener(e -> accountInfoResource.setValue(Resource.error("Tải ảnh thất bại: " + e.getMessage(), null)));
        };

        AccountInformation currentUser = accountInfoResource.getValue() != null ? accountInfoResource.getValue().getData() : null;
        if (currentUser != null && currentUser.getAvatar_url() != null && currentUser.getAvatar_url().startsWith("https://firebasestorage.googleapis.com")) {
            FirebaseStorage.getInstance().getReferenceFromUrl(currentUser.getAvatar_url()).delete().addOnCompleteListener(task -> uploadAction.run());
        } else {
            uploadAction.run();
        }
    }

    public void updateAccountProfile(UpdateAccountRequest request) {
        final LiveData<Resource<AccountInformation>> source = accountRepository.updateAccountProfile(request);
        accountInfoResource.addSource(source, resource -> {
            accountInfoResource.setValue(resource);
            if (resource.getStatus() != Resource.Status.LOADING) {
                accountInfoResource.removeSource(source);
            }
        });
    }


    private MutableLiveData<Resource<List<Address>>> addressListResource;
    private MutableLiveData<Resource<List<Order>>> orderListResource;

    private String avatar_url;

    public String getAvatar_url() {
        if (avatar_url == null) {
            loadAccountInfo();
        }
        return avatar_url;
    }


    public LiveData<Resource<String>> changePassword(ChangePasswordRequest changePasswordRequest) {
        return accountRepository.changePassword(changePasswordRequest);
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

    //    public void cancelOrder(Long orderId) {
//        accountRepository.cancelOrder(orderId).observeForever(orderListResource::setValue);
//    }
    public void cancelOrder(Long orderId) {
        accountRepository.cancelOrder(orderId).observeForever(resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                // Sau khi hủy thành công, tải lại danh sách đơn hàng từ server
                loadOrderList();
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                // Xử lý lỗi nếu có, ví dụ: hiển thị thông báo
                // Có thể cần một MutableLiveData khác để thông báo lỗi cho View
            }
        });
    }


    public void receiveOrder(Long orderId) {
        accountRepository.receiveOrder(orderId).observeForever(orderListResource::setValue);
    }

    public void returnOrder(long orderId, String returnReason) {
        accountRepository.returnOrder(orderId, returnReason).observeForever(orderListResource::setValue);
    }
    public LiveData<Resource<Order>> reorderOrder(long orderId) {
        return accountRepository.reorderOrder(orderId);
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
