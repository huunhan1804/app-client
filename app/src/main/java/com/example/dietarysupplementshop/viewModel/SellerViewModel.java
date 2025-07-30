package com.example.dietarysupplementshop.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.interfaces.OrderAPI; // Cần import OrderAPI
import com.example.dietarysupplementshop.interfaces.RetrofitClient; // Cần import RetrofitClient
import com.example.dietarysupplementshop.repositories.Resource;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerViewModel extends ViewModel {

    private final OrderAPI orderAPI;
    private MutableLiveData<Resource<List<Order>>> sellerOrderListResource;

    public SellerViewModel() {
        this.orderAPI = RetrofitClient.getRetrofitInstance().create(OrderAPI.class);
    }

    public LiveData<Resource<List<Order>>> getSellerOrders(String status) {
        if (sellerOrderListResource == null) {
            sellerOrderListResource = new MutableLiveData<>();
            loadSellerOrders(status);
        } else {
            // Cập nhật lại nếu trạng thái thay đổi hoặc cần refresh
            loadSellerOrders(status);
        }
        return sellerOrderListResource;
    }

    private void loadSellerOrders(String status) {
        sellerOrderListResource.setValue(Resource.loading(null));

        orderAPI.getAllSellerOrders().enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Order> allOrders = response.body().getData();
                    List<Order> filteredOrders;
                    if (status != null && !status.equals("ALL")) {
                        filteredOrders = allOrders.stream()
                                .filter(order -> status.equalsIgnoreCase(order.getOrder_status()))
                                .collect(Collectors.toList());
                    } else {
                        filteredOrders = allOrders; // Lấy tất cả nếu status là ALL
                    }
                    sellerOrderListResource.setValue(Resource.success(filteredOrders));
                } else {
                    String errorMessage = "Lỗi tải đơn hàng của người bán.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    sellerOrderListResource.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                sellerOrderListResource.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
    }

    // Các hành động của người bán
    public void confirmOrder(long orderId) {
        sellerOrderListResource.setValue(Resource.loading(sellerOrderListResource.getValue() != null ? sellerOrderListResource.getValue().getData() : null));
        orderAPI.confirmOrder(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleSellerOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                sellerOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void cancelOrderBySeller(long orderId) {
        sellerOrderListResource.setValue(Resource.loading(sellerOrderListResource.getValue() != null ? sellerOrderListResource.getValue().getData() : null));
        orderAPI.cancelOrder(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleSellerOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                sellerOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void markOrderAsDeliveredByAgency(long orderId) {
        sellerOrderListResource.setValue(Resource.loading(sellerOrderListResource.getValue() != null ? sellerOrderListResource.getValue().getData() : null));
        orderAPI.markOrderAsDeliveredByAgency(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleSellerOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                sellerOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void approveReturnRefund(long orderId) {
        sellerOrderListResource.setValue(Resource.loading(sellerOrderListResource.getValue() != null ? sellerOrderListResource.getValue().getData() : null));
        orderAPI.approveReturnRefund(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleSellerOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                sellerOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void rejectReturnRefund(long orderId, String rejectionReason) {
        sellerOrderListResource.setValue(Resource.loading(sellerOrderListResource.getValue() != null ? sellerOrderListResource.getValue().getData() : null));
        orderAPI.rejectReturnRefund(orderId, rejectionReason).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleSellerOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                sellerOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    private void handleSellerOrderResponse(Response<ResponseModel<List<Order>>> response) {
        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
            // Sau khi hành động thành công, load lại danh sách đơn hàng để cập nhật UI
            String status = sellerOrderListResource.getValue() != null ? sellerOrderListResource.getValue().getData().get(0).getOrder_status() : null;
            loadSellerOrders(status); // Load lại với status hiện tại
        } else {
            String errorMessage = "Lỗi xử lý đơn hàng.";
            if (response.errorBody() != null) {
                try {
                    errorMessage = response.errorBody().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sellerOrderListResource.setValue(Resource.error(errorMessage, null));
        }
    }
}