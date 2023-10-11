package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.util.CircleAnimationUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoActivity extends AppCompatActivity {
    private ImageSlider imageSlider;

    private ImageButton backButton;

    private ImageView cartIcon;

    private ProductAdapter productAdapter;

    private TextView productName, productPrice;

    private List<Product> relatedProduct;

    private RatingBar ratingBar;

    private WebView productWebView;

    private RecyclerView rcvRelatedProduct;

    private static final int REQUEST_CODE_POPUP = 1002;
    private static final int REQUEST_CODE_POPUP_CONFIRM = 1003;
    private static final int REQUEST_CODE_POPUP_ADD_TO_CART = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        productName = findViewById(R.id.productNameTextView);
        productName.setText("Boga Tra hay Trà bổ gan Bogatra Học Viện Quân Y");

        productPrice = findViewById(R.id.productPriceTextView);
        productPrice.setText("200.000 đ");

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(4);

        productWebView = findViewById(R.id.productWebView);
        String productHtml = "<h2>Thông tin sản phẩm:</h2><p>Boga Tra hay Trà bổ gan Bogatra Học Viện Quân Y là sản phẩm trà hỗ trợ thanh nhiệt, giải độc gan được nghiên cứu và sản xuất bởi Trung tâm nghiên cứu ứng dụng sản xuất TPCN Học Viện Quân Y. Với thành phần 100% từ: Cà gai leo, Diệp hạ châu dằng, Actiso và Kim ngân hoa.</p><h3>Công dụng:</h3><ul><li>Hỗ trợ thanh nhiệt, giải độc hiệu quả gan.</li><li>Tăng cường các chức năng gan.</li><li>Giảm các triệu chứng thường gặp ở người suy giảm chức năng gan.</li><li>Cải thiện tiêu hóa và tăng cường sức khỏe.</li></ul><h3>Đối tượng sử dụng:</h3><ul><li>Người bị suy giảm chức năng gan.</li><li>Người gặp các triệu chứng: Mẩn ngứa, phát ban, nổi mề đay, rôm sảy, chán ăn.</li><li>Người bị vàng da và vàng mắt do gan.</li><li>Người sử dụng rượu bia, thuốc có hại cho gan.</li><li>Người có nhu cầu tăng cường sức khỏe.</li></ul><h3>Hướng dẫn sử dụng:</h3><ul><li>Cho túi trà vào cốc chứa 100-150ml nước sôi. Để từ 3-5 phút rồi sử dụng.</li><li>Ngày uống dưới 5 gói.</li></ul><h3>Lưu ý:</h3><ul><li>Bảo quản: Nơi khô mát, tránh ánh sáng trực tiếp. Để xa tầm tay trẻ em.</li><li>Không dùng cho người mẫn cảm với thành phần nào của sản phẩm.</li><li>Phụ nữ có thai và trẻ em dưới 6 tuổi tham khảo ý kiến bác sỹ trước khi dùng.</li><li>Không sử dụng quá 5 gói trên ngày.</li><li>Hạn chế sử dụng các chất có hại cho gan như bia rượu,…</li><li>Sản phẩm này không phải là thuốc, không có tác dụng thay thế thuốc chữa bệnh.</li></ul>";
        productWebView.loadData(productHtml, "text/html", "UTF-8");

        imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<SlideModel>();
        slideModels.add(new SlideModel(R.drawable.product_image, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.product_image, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        rcvRelatedProduct = findViewById(R.id.recyclerView);
        relatedProduct = new ArrayList<>();
        relatedProduct.add(new Product(1, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(2, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(3, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(4, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(5, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(6, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));

        productAdapter = new ProductAdapter(relatedProduct, ProductInfoActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rcvRelatedProduct.setLayoutManager(gridLayoutManager);
        rcvRelatedProduct.setAdapter(productAdapter);

        ImageView messengerIcon = findViewById(R.id.messengerIcon);
        cartIcon = findViewById(R.id.cartIcon);
        TextView buyNowText = findViewById(R.id.buyNowText);

        messengerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý khi nhấn vào nút Messenger
                showPopup(view);
            }
        });

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupAddToCart(view);
            }
        });


        buyNowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý khi nhấn vào nút Buy Now
                showPopup(view);
            }
        });


    }

    private void showPopupAddToCart(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_to_cart_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set an animation for the popup to slide up from the bottom
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        popupView.startAnimation(slideUp);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        AddToCartHandling popupHandler = new AddToCartHandling(this, popupView, REQUEST_CODE_POPUP_ADD_TO_CART);
        popupHandler.setPopupWindow(popupWindow);

        Context context = this;
        popupHandler.setOnActivityResultListener(new AddToCartHandling.OnActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_CODE_POPUP_ADD_TO_CART && resultCode == RESULT_OK) {
                    if (data != null) {
                        makeFlyAnimation(imageSlider);
                    }
                }
            }
        });
    }

    private void showPopup(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.buy_now_popup_layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set an animation for the popup to slide up from the bottom
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        popupView.startAnimation(slideUp);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        PopupEventHandling popupHandler = new PopupEventHandling(this, popupView, REQUEST_CODE_POPUP);
        popupHandler.setPopupWindow(popupWindow);

        Context context = this;
        popupHandler.setOnActivityResultListener(new PopupEventHandling.OnActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_CODE_POPUP && resultCode == RESULT_OK) {
                    if (data != null) {
                        ShowPopUpConfirm(data, context, view);
                    }
                }
            }
        });


    }

    private void ShowPopUpConfirm(Intent data, Context context, View view){
        String productId =  data.getStringExtra("productId");
        int quantity = data.getIntExtra("quantity", 0);
        String name =  data.getStringExtra("name");
        String phone =  data.getStringExtra("phone");
        String address = data.getStringExtra("address");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView2 = inflater.inflate(R.layout.confirm_payment_popup_layout, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView2, width, height, focusable);

        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
        popupView2.startAnimation(slideUp);

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        ConfirmPopupHandling popupHandler = new ConfirmPopupHandling(context, popupView2, REQUEST_CODE_POPUP_CONFIRM, String.valueOf(quantity), "200.000 đ", name, phone, address);
        popupHandler.setPopupWindow(popupWindow);
        popupHandler.setOnActivityResultListener(new ConfirmPopupHandling.OnActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_CODE_POPUP_CONFIRM && resultCode == RESULT_OK) {
                    if (data != null) {
                        Log.d("MyTag", "Data: " + data.getStringExtra("address"));
                        Intent intent = new Intent(context, OrderSuccessActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    private void makeFlyAnimation(ImageSlider targetView) {
        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000).setDestView(cartIcon).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(ProductInfoActivity.this, "Continue Shopping...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();


    }


}