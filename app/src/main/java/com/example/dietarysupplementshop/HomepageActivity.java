package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dietarysupplementshop.adapter.ViewPagerAdapter;
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

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

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

        // Đặt sự kiện cho phím "Enter"
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Xử lý khi người dùng bấm "Enter"
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    // Phương thức xử lý tìm kiếm
    private void performSearch() {
//        String searchText = searchEditText.getText().toString();
//        Bundle bundle = new Bundle();
//        bundle.putString("searchText", searchText);
//        SeachResultProductFragment searchResultFragment = new SeachResultProductFragment();
//        searchResultFragment.setArguments(bundle);
        viewPager2.setCurrentItem(4);
    }
}