package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dietarysupplementshop.CartFragment;
import com.example.dietarysupplementshop.HomeFragment;
import com.example.dietarysupplementshop.OrderedFragment;
// import com.example.dietarysupplementshop.ProfileFragment; // Không trực tiếp tạo ở đây nữa
// import com.example.dietarysupplementshop.ProfileActivity; // Không liên quan

import com.example.dietarysupplementshop.BuyerProfileOverviewFragment; // THÊM IMPORT NÀY
import com.example.dietarysupplementshop.ProfileFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new OrderedFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new BuyerProfileOverviewFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}