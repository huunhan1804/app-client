package com.example.dietarysupplementshop;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.OrderDetail;
import com.example.dietarysupplementshop.model.Shop;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.responses.ProductInformation;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.util.CircleAnimationUtil;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductInfoActivity extends AppCompatActivity {

    private static final int COLLAPSED_MAX_LINES = 6;
    private static final int REQUEST_CODE_ADDRESS_LIST = 1001;
    private static final int REQUEST_CODE_POPUP = 1002;
    private static final int REQUEST_CODE_POPUP_CONFIRM = 1003;
    private static final int REQUEST_CODE_POPUP_ADD_TO_CART = 1004;

    private ImageSlider imageSlider;
    private ImageButton backButton;
    private ImageView cartIcon, chatIcon, shopAvatarImageView;
    private Button btnViewShop;
    private TextView productName, productPrice, productDescription, readMoreTextView, shopNameTextView, relatedProductsTitle;
    private RatingBar ratingBar;
    private RecyclerView rcvRelatedProduct;
    private FrameLayout frameLayout;
    private LottieAnimationView animationView;

    private ProductAdapter relatedProductAdapter;
    private ProductViewModel productViewModel;
    private AccountViewModel accountViewModel;

    private boolean isDescriptionExpanded = false;
    private ProductInformation currentProductInformation;
    private View viewForPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        initViews();
        initViewModels();
        setupRecyclerViews();
        setupStaticClickListeners();
        getIntentData();
    }

    private void initViews() {
        imageSlider = findViewById(R.id.imageSlider);
        backButton = findViewById(R.id.backButton);
        cartIcon = findViewById(R.id.cartIcon);
        chatIcon = findViewById(R.id.messengerIcon);
        btnViewShop = findViewById(R.id.viewShopButton);
        productName = findViewById(R.id.productNameTextView);
        productPrice = findViewById(R.id.productPriceTextView);
        productDescription = findViewById(R.id.productDescriptionTextView);
        readMoreTextView = findViewById(R.id.readMoreTextView);
        ratingBar = findViewById(R.id.ratingBar);
        shopAvatarImageView = findViewById(R.id.shopAvatarImageView);
        shopNameTextView = findViewById(R.id.shopNameTextView);
        rcvRelatedProduct = findViewById(R.id.recyclerView);
        relatedProductsTitle = findViewById(R.id.textRelatedProduct);
    }

    private void initViewModels() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        accountViewModel = MyApplication.getInstance().getAccountViewModel();
    }

    private void setupRecyclerViews() {
        relatedProductAdapter = new ProductAdapter(new ArrayList<>(), this);
        rcvRelatedProduct.setLayoutManager(new GridLayoutManager(this, 2));
        rcvRelatedProduct.setAdapter(relatedProductAdapter);
    }

    private void getIntentData() {
        long productId = getIntent().getLongExtra("productId", -1);
        if (productId != -1) {
            observeProductData(productId);
            observeRelatedProducts(productId);
        } else {
            Toast.makeText(this, "Không có thông tin sản phẩm.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupStaticClickListeners() {
        backButton.setOnClickListener(v -> finish());
        readMoreTextView.setOnClickListener(v -> toggleDescription());
        chatIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });
    }

    private void observeProductData(long productId) {
        productViewModel.getProductInformation(productId).observe(this, productInfo -> {
            if (productInfo != null) {
                Shop fakeShop = new Shop(1L, "Cửa Hàng Dinh Dưỡng ABC (Test)", "https://picsum.photos/id/1025/200");
                productInfo.setShop(fakeShop);

                updateUI(productInfo);
            }
        });
    }
    private void observeRelatedProducts(long productId) {
        productViewModel.getListRelatedProduct(productId).observe(this, products -> {
            if (products != null && !products.isEmpty()) {
                relatedProductsTitle.setVisibility(View.VISIBLE);
                rcvRelatedProduct.setVisibility(View.VISIBLE);
                relatedProductAdapter.updateProducts(products);
            } else {
                relatedProductsTitle.setVisibility(View.GONE);
                rcvRelatedProduct.setVisibility(View.GONE);
            }
        });
    }

    private void updateUI(ProductInformation productInfo) {
        productName.setText(productInfo.getProduct_name());
        productPrice.setText(productInfo.getProduct_price());
        ratingBar.setRating((float) productInfo.getRating());

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        if (productInfo.getMedia_url() != null) {
            for (String image : productInfo.getMedia_url()) {
                slideModels.add(new SlideModel(image, ScaleTypes.FIT));
            }
        }
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        updateDescription(productInfo.getProduct_description());
        updateShopInfo(productInfo.getShop());
        setupDynamicClickListeners(productInfo);
    }

    private void updateShopInfo(Shop shop) {
        if (shop != null) {
            findViewById(R.id.shopInfoContainer).setVisibility(View.VISIBLE);
            shopNameTextView.setText(shop.getShopName());
            Glide.with(this).load(shop.getAvatarUrl()).placeholder(R.drawable.logo_2).into(shopAvatarImageView);
        } else {
            findViewById(R.id.shopInfoContainer).setVisibility(View.GONE);
        }
    }

    private void setupDynamicClickListeners(ProductInformation productInfo) {
        Shop shop = productInfo.getShop();
        if (shop != null) {
            btnViewShop.setOnClickListener(v -> {
                Intent intent = new Intent(ProductInfoActivity.this, ShopSellerActivity.class);
                intent.putExtra("SHOP_ID", shop.getShopId());
                intent.putExtra("SHOP_NAME", shop.getShopName());
                startActivity(intent);
            });
        }

        findViewById(R.id.buyNowText).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Mua ngay", Toast.LENGTH_SHORT).show();
            showPopupBuyNow(v, productInfo);
        });
        cartIcon.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Thêm vào giỏ", Toast.LENGTH_SHORT).show();
            showPopupAddToCart(v, productInfo);
        });
    }

    private void updateDescription(String descriptionHtml) {
        if (descriptionHtml == null || descriptionHtml.isEmpty()) {
            productDescription.setVisibility(View.GONE);
            readMoreTextView.setVisibility(View.GONE);
            return;
        }

        productDescription.setVisibility(View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            productDescription.setText(Html.fromHtml(descriptionHtml, Html.FROM_HTML_MODE_LEGACY));
        } else {
            productDescription.setText(Html.fromHtml(descriptionHtml));
        }

        productDescription.post(() -> {
            int actualLineCount = productDescription.getLineCount();
            Log.d("DescriptionDebug", "COLLAPSED_MAX_LINES: " + COLLAPSED_MAX_LINES + ", Actual lines rendered: " + actualLineCount);

            if (actualLineCount > COLLAPSED_MAX_LINES) {
                productDescription.setMaxLines(COLLAPSED_MAX_LINES);
                readMoreTextView.setVisibility(View.VISIBLE);
                readMoreTextView.setText("Xem thêm");
                isDescriptionExpanded = false;
            } else {
                readMoreTextView.setVisibility(View.GONE);
                Log.d("DescriptionDebug", "Hiding Read More button because actual lines <= COLLAPSED_MAX_LINES");
            }
        });
    }

    private void toggleDescription() {
        if (isDescriptionExpanded) {
            productDescription.setMaxLines(COLLAPSED_MAX_LINES);
            readMoreTextView.setText("Xem thêm");
            isDescriptionExpanded = false;
        } else {
            productDescription.setMaxLines(Integer.MAX_VALUE);
            readMoreTextView.setText("Thu gọn");
            isDescriptionExpanded = true;
        }

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(300);
        productDescription.startAnimation(fadeIn);
    }

    private void showPopupAddToCart(View view, ProductInformation productInformation) {
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
                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.buy_now_popup_layout, null);

                            int width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true;
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                            popupView.startAnimation(slideUp);

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

    private void ShowPopUpConfirm(Intent data, Context context, View view) {
        long productId = data.getLongExtra("productId", 0);
        long productVariantId = data.getLongExtra("productVariantId", 0);
        String productPrice = data.getStringExtra("productPrice");
        int quantity = data.getIntExtra("quantity", 0);
        String name = data.getStringExtra("name");
        String phone = data.getStringExtra("phone");
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
                    String subTotal = data1.getStringExtra("subTotal");
                    String totalBill = data1.getStringExtra("totalBill");
                    String shippingInfo = data1.getStringExtra("address");

                    List<OrderDetail> orderDetail = new ArrayList<>();
                    orderDetail.add(new OrderDetail(productId, productVariantId, quantity, productPrice, subTotal));
                    OrderRequest orderRequest = new OrderRequest(shippingInfo, totalBill, orderDetail);
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