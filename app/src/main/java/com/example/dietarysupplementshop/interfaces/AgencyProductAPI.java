package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductFullDTO;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AgencyProductAPI {

    // Lấy danh sách sản phẩm theo trạng thái
    @GET("api/agency/products")
    Call<ResponseModel<List<ProductInfoDTO>>> getAgencyProductsByStatus(@Query("status_code") String statusCode);

    // Lấy chi tiết sản phẩm để sửa
    @GET("api/agency/products/{productId}")
    Call<ResponseModel<ProductFullDTO>> getProductDetails(@Path("productId") Long productId);

    // Thêm sản phẩm mới
    @POST("api/agency/products")
    Call<ResponseModel<ProductFullDTO>> createProduct(@Body AddNewProductRequest request);

    // Cập nhật sản phẩm
    @PUT("api/agency/products/{productId}")
    Call<ResponseModel<ProductFullDTO>> updateProduct(@Path("productId") Long productId, @Body UpdateProductRequest request);

    // Xóa sản phẩm
    @DELETE("api/agency/products/{productId}")
    Call<ResponseModel<Void>> deleteProduct(@Path("productId") Long productId);

    // Thêm phương thức để cập nhật trạng thái bán hàng
    @POST("api/agency/products/{productId}/disable")
    Call<ResponseModel<ProductInfoDTO>> disableSellingProduct(@Path("productId") long productId);
}