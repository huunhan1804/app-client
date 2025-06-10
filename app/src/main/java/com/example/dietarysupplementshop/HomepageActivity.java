package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.adapter.ViewPagerAdapter;
import com.example.dietarysupplementshop.token.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class HomepageActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private OrderedFragment orderedFragment;
    private CartFragment cartFragment;
    private ProfileFragment profileFragment;
    private ViewPager viewPager2;

    private TextInputLayout searchTextInputLayout;
    private TextInputEditText searchEditText;
    private FrameLayout frameLayout;
    LottieAnimationView animationView;

    private BottomNavigationView bottomNavigationView;

    private ImageView locationIcon;
    private ImageView chatIcon;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        TokenManager tokenManager = new TokenManager(getApplicationContext());
        String accessToken = tokenManager.getAccessToken();

        

        if (accessToken == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        locationIcon = findViewById(R.id.locationIcon);
        locationIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, LocationStoreActivity.class);
            startActivity(intent);
        });

        chatIcon = findViewById(R.id.chatIcon);
        chatIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });


        homeFragment = new HomeFragment();
        orderedFragment = new OrderedFragment();
        cartFragment = new CartFragment();
        profileFragment = new ProfileFragment();

        viewPager2 = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_ordered).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_cart).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                viewPager2.setCurrentItem(0);
            } else if (item.getItemId() == R.id.menu_ordered) {
                viewPager2.setCurrentItem(1);
            } else if (item.getItemId() == R.id.menu_cart) {
                viewPager2.setCurrentItem(2);
            } else if (item.getItemId() == R.id.menu_profile) {
                viewPager2.setCurrentItem(3);
            }
            return false;
        });


        searchTextInputLayout = findViewById(R.id.searchTextInputLayout);
        searchEditText = findViewById(R.id.searchEditText);

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Intent intent = new Intent(HomepageActivity.this, SearchActivity.class);
                startActivity(intent);
                searchEditText.clearFocus();
            }
        });


        if (getIntent() != null && "CartFragment".equals(getIntent().getStringExtra("navigateTo"))) {
            viewPager2.setCurrentItem(2);
        }

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