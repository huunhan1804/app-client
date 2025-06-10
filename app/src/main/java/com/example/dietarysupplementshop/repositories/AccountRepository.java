package com.example.dietarysupplementshop.repositories;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.AccountAPI;
import com.example.dietarysupplementshop.interfaces.AddressAPI;
import com.example.dietarysupplementshop.interfaces.OrderAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddAddressRquest;
import com.example.dietarysupplementshop.requests.AddLoginIdRequest;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.ChangePasswordRequest;
import com.example.dietarysupplementshop.requests.CheckoutRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.requests.UpdateAccountRequest;
import com.example.dietarysupplementshop.requests.UpdateAddressRequest;
import com.example.dietarysupplementshop.requests.UpdateAvatarRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private final AccountAPI accountAPI;

    private final AddressAPI addressAPI;

    private final OrderAPI orderAPI;

    private AccountInformation cachedAccountInfo;

    private List<Address> cachedAddress;
    private List<Order> cachedOrders;


    public AccountRepository() {
        this.accountAPI = RetrofitClient.getRetrofitInstance().create(AccountAPI.class);
        this.addressAPI = RetrofitClient.getRetrofitInstance().create(AddressAPI.class);
        this.orderAPI = RetrofitClient.getRetrofitInstance().create(OrderAPI.class);
    }


    public LiveData<Resource<AccountInformation>> fetchAccount() {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        if (cachedAccountInfo != null) {
            data.setValue(Resource.success(cachedAccountInfo));
            return data;
        }

        accountAPI.getCurrentUser().enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> fetchUpdateAvatar(String avatar_url) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.updateAvatar(new UpdateAvatarRequest(avatar_url)).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> fetchAddLoginId(String loginId) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.addLoginId(new AddLoginIdRequest(loginId)).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<String>> changePassword(ChangePasswordRequest request) {
        MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.changePassword(request).enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<String>> call, @NonNull Response<ResponseModel<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getMessage()));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<String>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> updateAccountProfile(UpdateAccountRequest request) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.updateAccountProfile(request).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> fetchAddToCart(AddToCartRequest request) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.addToCart(request).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> fetchIncreaseCartItemQuantity(AddToCartRequest request) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.increaseCartItemQuantity(request).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> fetchDecreaseCartItemQuantity(AddToCartRequest request) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.decreaseCartItemQuantity(request).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<AccountInformation>> fetchDeleteCartItem(long cart_item_id) {
        MutableLiveData<Resource<AccountInformation>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        accountAPI.deleteCartItem(cart_item_id).enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Response<ResponseModel<AccountInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountInformation accountInfo = response.body().getData();
                    cachedAccountInfo = accountInfo;
                    data.setValue(Resource.success(accountInfo));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AccountInformation>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }


    public LiveData<Resource<List<Address>>> fetchAddressList() {
        MutableLiveData<Resource<List<Address>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        if (cachedAddress != null) {
            data.setValue(Resource.success(cachedAddress));
            return data;
        }

        addressAPI.getListAddress().enqueue(new Callback<ResponseModel<List<Address>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Response<ResponseModel<List<Address>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Address> addresses = new Gson().fromJson(new Gson().toJson(response.body().getData()), new TypeToken<List<Address>>() {}.getType());
                    data.setValue(Resource.success(addresses));
                    cachedAddress = addresses;
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<Address>> fetchInfoAddress(long addressId) {
        MutableLiveData<Resource<Address>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        addressAPI.getInfoAddress(addressId).enqueue(new Callback<ResponseModel<Address>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<Address>> call, @NonNull Response<ResponseModel<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<Address>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<Address>>> fetchSetDefaultAddress(Long addressId) {
        MutableLiveData<Resource<List<Address>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        addressAPI.setDefaultAddress(addressId).enqueue(new Callback<ResponseModel<List<Address>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Response<ResponseModel<List<Address>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Address>>() {}.getType();
                    List<Address> addresses = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(addresses));
                    cachedAddress = addresses;
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<Address>>> addAddress(AddAddressRquest addAddressRquest) {
        MutableLiveData<Resource<List<Address>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        addressAPI.addAddress(addAddressRquest).enqueue(new Callback<ResponseModel<List<Address>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Response<ResponseModel<List<Address>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Address>>() {}.getType();
                    List<Address> addresses = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(addresses));
                    cachedAddress = addresses;
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<Address>>> updateAddress(UpdateAddressRequest request) {
        MutableLiveData<Resource<List<Address>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        addressAPI.updateAddress(request).enqueue(new Callback<ResponseModel<List<Address>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Response<ResponseModel<List<Address>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Address>>() {}.getType();
                    List<Address> addresses = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(addresses));
                    cachedAddress = addresses;
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Address>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }


    public LiveData<Resource<List<Order>>> fetchOrderList() {
        MutableLiveData<Resource<List<Order>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        if (cachedOrders != null) {
            data.setValue(Resource.success(cachedOrders));
            return data;
        }

        orderAPI.getAllOrdered().enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderList = new Gson().fromJson(new Gson().toJson(response.body().getData()), new TypeToken<List<Order>>() {}.getType());
                    data.setValue(Resource.success(orderList));
                    cachedOrders = orderList;
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<Order>> getOrderInfo(long orderId) {
        MutableLiveData<Resource<Order>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        orderAPI.getOrderDetail(orderId).enqueue(new Callback<ResponseModel<Order>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<Order>> call, @NonNull Response<ResponseModel<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order order = new Gson().fromJson(new Gson().toJson(response.body().getData()), new TypeToken<Order>() {}.getType());
                    data.setValue(Resource.success(order));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<Order>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<Order>>> cancelOrder(long orderId) {
        MutableLiveData<Resource<List<Order>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        orderAPI.cancelOrder(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderList = new Gson().fromJson(new Gson().toJson(response.body().getData()), new TypeToken<List<Order>>() {}.getType());
                    data.setValue(Resource.success(orderList));
                    cachedOrders = orderList;
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<Order>> addOrder(OrderRequest request) {
        MutableLiveData<Resource<Order>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        orderAPI.addOrder(request).enqueue(new Callback<ResponseModel<Order>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<Order>> call, @NonNull Response<ResponseModel<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order order = new Gson().fromJson(new Gson().toJson(response.body().getData()), new TypeToken<Order>() {}.getType());
                    data.setValue(Resource.success(order));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<Order>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    public LiveData<Resource<List<OrderDetailResponse>>> getOrderDetailCheckout(CheckoutRequest request) {
        MutableLiveData<Resource<List<OrderDetailResponse>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        orderAPI.getOrderDetailCheckout(request).enqueue(new Callback<ResponseModel<List<OrderDetailResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<OrderDetailResponse>>> call, @NonNull Response<ResponseModel<List<OrderDetailResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderDetailResponse> order = new Gson().fromJson(new Gson().toJson(response.body().getData()), new TypeToken<List<OrderDetailResponse>>() {}.getType());
                    data.setValue(Resource.success(order));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<OrderDetailResponse>>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }


    private <T> void handleErrorResponse(Response<?> response, MutableLiveData<Resource<T>> data) {
        if (response.errorBody() != null && response.code() != 403 && response.code() != 401) {
            try {
                String errorJsonString = response.errorBody().string();
                ResponseModel<?> errorResponseModel = new Gson().fromJson(errorJsonString, ResponseModel.class);
                data.setValue(Resource.error(errorResponseModel.getMessage(), null));
            } catch (IOException e) {
                data.setValue(Resource.error("Unknown error occurred", null));
            }
        }
    }

    public void reloadAccountInfo() {
        cachedAccountInfo = null;
        fetchAccount();
    }

    public void reloadAddressList() {
        cachedAddress = null;
        fetchAddressList();
    }

    public void reloadOrderList() {
        cachedOrders = null;
        fetchOrderList();
    }





}
