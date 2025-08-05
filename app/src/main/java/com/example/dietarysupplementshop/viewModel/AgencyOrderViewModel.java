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

public class AgencyOrderViewModel extends ViewModel {

    private final OrderAPI orderAPI;
    private MutableLiveData<Resource<List<Order>>> agencyOrderListResource;

    public AgencyOrderViewModel() {
        this.orderAPI = RetrofitClient.getRetrofitInstance().create(OrderAPI.class);
    }

    public LiveData<Resource<List<Order>>> getAgencyOrders(String status) {
        if (agencyOrderListResource == null) {
            agencyOrderListResource = new MutableLiveData<>();
            loadAgencyOrders(status);
        } else {
            // Cập nhật lại nếu trạng thái thay đổi hoặc cần refresh
            loadAgencyOrders(status);
        }
        return agencyOrderListResource;
    }

    private void loadAgencyOrders(String status) {
        agencyOrderListResource.setValue(Resource.loading(null));

        orderAPI.getAllAgencyOrders().enqueue(new Callback<ResponseModel<List<Order>>>() {
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
                    agencyOrderListResource.setValue(Resource.success(filteredOrders));
                } else {
                    String errorMessage = "Lỗi tải đơn hàng của người bán.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    agencyOrderListResource.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                agencyOrderListResource.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
    }

    // Các hành động của người bán
    public void confirmOrder(long orderId) {
        agencyOrderListResource.setValue(Resource.loading(agencyOrderListResource.getValue() != null ? agencyOrderListResource.getValue().getData() : null));
        orderAPI.confirmOrder(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleAgencyOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                agencyOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void cancelOrderByAgency(long orderId) {
        agencyOrderListResource.setValue(Resource.loading(agencyOrderListResource.getValue() != null ? agencyOrderListResource.getValue().getData() : null));
        orderAPI.cancelOrder(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleAgencyOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                agencyOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void markOrderAsDeliveredByAgency(long orderId) {
        agencyOrderListResource.setValue(Resource.loading(agencyOrderListResource.getValue() != null ? agencyOrderListResource.getValue().getData() : null));
        orderAPI.markOrderAsDeliveredByAgency(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleAgencyOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                agencyOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void approveReturnRefund(long orderId) {
        agencyOrderListResource.setValue(Resource.loading(agencyOrderListResource.getValue() != null ? agencyOrderListResource.getValue().getData() : null));
        orderAPI.approveReturnRefund(orderId).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleAgencyOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                agencyOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    public void rejectReturnRefund(long orderId, String rejectionReason) {
        agencyOrderListResource.setValue(Resource.loading(agencyOrderListResource.getValue() != null ? agencyOrderListResource.getValue().getData() : null));
        orderAPI.rejectReturnRefund(orderId, rejectionReason).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Response<ResponseModel<List<Order>>> response) {
                handleAgencyOrderResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<List<Order>>> call, @NonNull Throwable t) {
                agencyOrderListResource.setValue(Resource.error(t.getMessage(), null));
            }
        });
    }

    private void handleAgencyOrderResponse(Response<ResponseModel<List<Order>>> response) {
        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
            // Sau khi hành động thành công, load lại danh sách đơn hàng để cập nhật UI
            String status = agencyOrderListResource.getValue() != null ? agencyOrderListResource.getValue().getData().get(0).getOrder_status() : null;
            loadAgencyOrders(status); // Load lại với status hiện tại
        } else {
            String errorMessage = "Lỗi xử lý đơn hàng.";
            if (response.errorBody() != null) {
                try {
                    errorMessage = response.errorBody().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            agencyOrderListResource.setValue(Resource.error(errorMessage, null));
        }
    }
}