package com.example.dietarysupplementshop.repositories; // Hoặc com.example.dietarysupplementshop.repositories.mocks; nếu bạn tạo thư mục con

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockCouponRepository implements CouponRepository {

    @Override
    public LiveData<Resource<List<AccountCoupon>>> getUserCoupons(String statusFilter) {
        MutableLiveData<Resource<List<AccountCoupon>>> data = new MutableLiveData<>();
        // Giả lập trạng thái LOADING
        data.setValue(Resource.loading(null));

        // Tạo danh sách các voucher giả lập
        List<AccountCoupon> mockCoupons = new ArrayList<>();

        // Voucher có thể dùng (USABLE)
        mockCoupons.add(new AccountCoupon(new Coupon("VC001", "Giảm 20.000 VNĐ cho đơn hàng từ 100k", 20000.0, "PRODUCT_DISCOUNT"), "USABLE", "01/07/2024", "31/12/2024"));
        mockCoupons.add(new AccountCoupon(new Coupon("VC002", "Giảm 10% tối đa 50k cho tất cả sản phẩm", 0.1, "PERCENT_DISCOUNT"), "USABLE", "05/07/2024", "25/08/2024"));
        mockCoupons.add(new AccountCoupon(new Coupon("VC003", "Miễn phí vận chuyển cho đơn hàng trên 200k", 25000.0, "SHIPPING_FEE"), "USABLE", "10/07/2024", "20/07/2024"));
        mockCoupons.add(new AccountCoupon(new Coupon("VC006", "Voucher thử nghiệm - Còn 1 ngày", 10000.0, "PRODUCT_DISCOUNT"), "USABLE", "16/07/2024", "17/07/2024"));


        // Voucher đã dùng (USED)
        mockCoupons.add(new AccountCoupon(new Coupon("VC004", "Giảm 50.000 VNĐ đã dùng", 50000.0, "PRODUCT_DISCOUNT"), "USED", "01/06/2024", "15/06/2024"));

        // Voucher đã hết hạn (EXPIRED)
        mockCoupons.add(new AccountCoupon(new Coupon("VC005", "Giảm 30% đã hết hạn", 0.3, "PERCENT_DISCOUNT"), "EXPIRED", "01/05/2024", "31/05/2024"));

        // Lọc dữ liệu theo trạng thái nếu có yêu cầu
        List<AccountCoupon> filteredCoupons;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            filteredCoupons = mockCoupons.stream()
                    .filter(coupon -> coupon.getStatus().equals(statusFilter))
                    .collect(Collectors.toList());
        } else {
            filteredCoupons = mockCoupons.stream()
                    .filter(coupon -> coupon.getStatus().equals("USABLE"))
                    .collect(Collectors.toList());
        }

        // Giả lập độ trễ mạng và sau đó gửi dữ liệu thành công
        new android.os.Handler().postDelayed(() -> {
            // data.setValue(Resource.success(new ArrayList<>())); // Để test trạng thái rỗng
            // data.setValue(Resource.error("Lỗi kết nối máy chủ giả lập.", null)); // Để test trạng thái lỗi
            data.setValue(Resource.success(filteredCoupons));
        }, 1000); // Giả lập độ trễ 1 giây

        return data;
    }

    @Override
    public LiveData<Resource<AppliedCouponResponse>> applyCoupon(ApplyCouponRequest request) {
        MutableLiveData<Resource<AppliedCouponResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        new android.os.Handler().postDelayed(() -> {
            if (request != null && "VC001".equals(request.getCouponCode())) {
                AppliedCouponResponse successResponse = new AppliedCouponResponse("Áp dụng voucher " + request.getCouponCode() + " thành công! Giỏ hàng đã được cập nhật.", true);
                data.setValue(Resource.success(successResponse));
            } else if (request != null && "VC002".equals(request.getCouponCode())) {
                data.setValue(Resource.error("Voucher " + request.getCouponCode() + " không áp dụng được cho đơn hàng này (giả lập lỗi).", null));
            } else {
                AppliedCouponResponse generalSuccess = new AppliedCouponResponse("Áp dụng voucher thành công!", true);
                data.setValue(Resource.success(generalSuccess));
            }
        }, 800); // Giả lập độ trễ 0.8 giây
        return data;
    }

    @Override
    public LiveData<Resource<AppliedCouponResponse>> removeCoupon(ApplyCouponRequest request) {
        MutableLiveData<Resource<AppliedCouponResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        new android.os.Handler().postDelayed(() -> {
            AppliedCouponResponse successResponse = new AppliedCouponResponse("Hủy voucher " + request.getCouponCode() + " thành công!", true);
            data.setValue(Resource.success(successResponse));
        }, 500); // Giả lập độ trễ 0.5 giây
        return data;
    }
}