package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dietarysupplementshop.SellerOrderFragment;

public class SellerOrderPagerAdapter extends FragmentStateAdapter {

    public SellerOrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String status = "";
        switch (position) {
            case 0:
                status = "PENDING";
                break;
            case 1:
                status = "SHIPPING";
                break;
            case 2:
                status = "DELIVERED";
                break;
            case 3:
                status = "CANCELLED";
                break;
            case 4:
                status = "RETURNED";
                break;
            case 5:
                status = "DELIVERY FAILED";
                break;
        }
        return SellerOrderFragment.newInstance(status);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}