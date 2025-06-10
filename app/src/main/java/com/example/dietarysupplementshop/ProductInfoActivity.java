package com.example.dietarysupplementshop;

import static com.example.dietarysupplementshop.repositories.Resource.Status.ERROR;
import static com.example.dietarysupplementshop.repositories.Resource.Status.LOADING;
import static com.example.dietarysupplementshop.repositories.Resource.Status.SUCCESS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dietarysupplementshop.adapter.FakeProductAdapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.OrderDetail;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.responses.ProductInformation;
import com.example.dietarysupplementshop.util.CircleAnimationUtil;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoActivity extends AppCompatActivity {
    private ImageSlider imageSlider;

    private ImageButton backButton;

    private ImageView cartIcon;
    private ImageView chatIcon;

    private ProductAdapter productAdapter;

    private TextView productName, productPrice;

    private List<Product> relatedProduct;

    private RatingBar ratingBar;

    private WebView productWebView;

    private RecyclerView rcvRelatedProduct;

    private ProductViewModel productViewModel;

    private AccountViewModel accountViewModel;

    private FrameLayout frameLayout;
    private LottieAnimationView animationView;

    private ProductInformation productInformation;
    private View view;
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

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        accountViewModel = MyApplication.getInstance().getAccountViewModel();


        Intent intent = getIntent();
        if (intent != null) {
            long productId = intent.getLongExtra("productId", -1);
            if (productId != -1) {
                productViewModel.getProductInformation(productId).observe(this, productInfo -> {
                    if (productInfo != null) {
                        productName = findViewById(R.id.productNameTextView);
                        productName.setText(productInfo.getProduct_name());

                        productPrice = findViewById(R.id.productPriceTextView);
                        productPrice.setText(productInfo.getProduct_price());


                        ratingBar = findViewById(R.id.ratingBar);
                        ratingBar.setRating(Float.parseFloat(String.valueOf(productInfo.getRating())));

                        productWebView = findViewById(R.id.productWebView);
                        productWebView.loadData(productInfo.getProduct_description(), "text/html", "UTF-8");

                        imageSlider = findViewById(R.id.imageSlider);
                        ArrayList<SlideModel> slideModels = new ArrayList<SlideModel>();
                        for (String image: productInfo.getMedia_url()) {
                            slideModels.add(new SlideModel(image, ScaleTypes.FIT));
                        }
                        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

                        rcvRelatedProduct = findViewById(R.id.recyclerView);
                        FakeProductAdapter fakeProductAdapter = new FakeProductAdapter(productViewModel.createFakeProducts(10));
                        rcvRelatedProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        rcvRelatedProduct.setAdapter(fakeProductAdapter);

                        productViewModel.getListRelatedProduct(productId).observe(this, products -> {
                            relatedProduct = products;
                            productAdapter = new ProductAdapter(relatedProduct, ProductInfoActivity.this);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                            rcvRelatedProduct.setLayoutManager(gridLayoutManager);
                            rcvRelatedProduct.setAdapter(productAdapter);
                        });

                        cartIcon = findViewById(R.id.cartIcon);
                        TextView buyNowText = findViewById(R.id.buyNowText);

                        cartIcon.setOnClickListener(view -> showPopupAddToCart(view, productInfo));

                        buyNowText.setOnClickListener(view1 -> {showPopupBuyNow(view1, productInfo); view = view1; productInformation = productInfo;});
                    }
                });
            }
        }

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

        chatIcon =  findViewById(R.id.messengerIcon);
        chatIcon.setOnClickListener(view -> {
            Intent intent2 = new Intent(this, ChatActivity.class);
            startActivity(intent2);
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (data != null) {
                long selectedAddress = data.getLongExtra("selectedAddress", 0);
                if (selectedAddress != 0) {
                    accountViewModel.getInfoAddress(selectedAddress).observe(this, resource -> {
                        switch (resource.getStatus()) {
                            case LOADING:
                                showProgressBar();
                                break;
                            case SUCCESS:
                                hideProgressBar();
                                if(resource.getData() != null){
                                    Address defaultAddress = resource.getData();

                                    if (defaultAddress != null) {
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

                                        PopupEventHandling popupHandler = new PopupEventHandling(this, popupView, REQUEST_CODE_POPUP, productInformation, defaultAddress);
                                        popupHandler.setPopupWindow(popupWindow);

                                        Context context = this;
                                        popupHandler.setOnActivityResultListener((requestCode1, resultCode1, data1) -> {
                                            if (requestCode1 == REQUEST_CODE_POPUP && resultCode1 == RESULT_OK) {
                                                if (data1 != null) {
                                                    ShowPopUpConfirm(data1, context, view);
                                                }
                                            }
                                        });
                                    }
                                }
                                break;
                            case ERROR:
                                hideProgressBar();
                                Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    });
                }

            }
        }
    }

    private void showPopupAddToCart(View view, ProductInformation productInformation){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_to_cart_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        popupView.startAnimation(slideUp);

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        AddToCartHandling popupHandler = new AddToCartHandling(this, popupView, REQUEST_CODE_POPUP_ADD_TO_CART, productInformation);
        popupHandler.setPopupWindow(popupWindow);

        popupHandler.setOnActivityResultListener((requestCode, resultCode, data) -> {
            if (requestCode == REQUEST_CODE_POPUP_ADD_TO_CART && resultCode == RESULT_OK) {
                if (data != null) {
                    showProgressBar();
                    AddToCartRequest request = (AddToCartRequest) data.getSerializableExtra("addToCartRequest");
                    accountViewModel.addToCart(request).observe(this, resource -> {
                        switch (resource.getStatus()) {
                            case LOADING:
                                showProgressBar();
                                break;
                            case SUCCESS:
                                hideProgressBar();
                                makeFlyAnimation(imageSlider);
                                break;
                            case ERROR:
                                hideProgressBar();
                                Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    });
                }
            }
        });
    }

    private void showPopupBuyNow(View view, ProductInformation productInformation) {
        accountViewModel.getAddressListResource().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    showProgressBar();
                    break;
                case SUCCESS:
                    hideProgressBar();
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        Address defaultAddress = resource.getData().stream()
                                .filter(Address::getIs_default)
                                .findFirst()
                                .orElse(resource.getData().get(0));

                        if (defaultAddress != null) {
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

                            PopupEventHandling popupHandler = new PopupEventHandling(this, popupView, REQUEST_CODE_POPUP, productInformation, defaultAddress);
                            popupHandler.setPopupWindow(popupWindow);

                            Context context = this;
                            popupHandler.setOnActivityResultListener((requestCode, resultCode, data) -> {
                                if (requestCode == REQUEST_CODE_POPUP && resultCode == RESULT_OK) {
                                    if (data != null) {
                                        ShowPopUpConfirm(data, context, view);
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this, "Please add an address to buy now", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, AddressInfoActivity.class);
                        startActivity(intent);
                    }
                    break;
                case ERROR:
                    hideProgressBar();
                    Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });

    }

    private void ShowPopUpConfirm(Intent data, Context context, View view){
        long productId =  data.getLongExtra("productId", 0);
        long productVariantId =  data.getLongExtra("productVariantId", 0);
        String productPrice = data.getStringExtra("productPrice");
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

        ConfirmPopupHandling popupHandler = new ConfirmPopupHandling(context, popupView2, REQUEST_CODE_POPUP_CONFIRM, String.valueOf(quantity), productPrice, name, phone, address);
        popupHandler.setPopupWindow(popupWindow);
        popupHandler.setOnActivityResultListener((requestCode, resultCode, data1) -> {
            if (requestCode == REQUEST_CODE_POPUP_CONFIRM && resultCode == RESULT_OK) {
                if (data1 != null) {
                    String subTotal =  data1.getStringExtra("subTotal");
                    String totalBill =  data1.getStringExtra("totalBill");
                    String shippingInfo =  data1.getStringExtra("address");

                    List<OrderDetail> orderDetail = new ArrayList<>();
                    orderDetail.add(new OrderDetail(productId, productVariantId, quantity, productPrice, subTotal));
                    OrderRequest orderRequest = new OrderRequest(shippingInfo, totalBill,orderDetail);
                    accountViewModel.addOrder(orderRequest).observe(this, orderResource -> {
                        if (orderResource != null) {
                            switch (orderResource.getStatus()) {
                                case LOADING:
                                    showProgressBar();
                                    break;
                                case SUCCESS:
                                    hideProgressBar();
                                    if (orderResource.getData() != null) {
                                        Intent intent = new Intent(context, OrderSuccessActivity.class);
                                        intent.putExtra("orderId", orderResource.getData().getOrder_id());
                                        startActivity(intent);
                                        finish();
                                    }
                                    break;
                                case ERROR:
                                    hideProgressBar();
                                    Toast.makeText(this, orderResource.getMessage(), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
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
                Toast.makeText(ProductInfoActivity.this, "Add to cart success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();


    }


    public void showProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);
    }

}