package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dietarysupplementshop.OrderedFragment;

public class OrdersViewPagerAdapter extends FragmentStateAdapter {

    public OrdersViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return OrderedFragment.newInstance("PENDING", null);
            case 1:
                return OrderedFragment.newInstance("SHIPPING", null);
            case 2:
                return OrderedFragment.newInstance("DELIVERED", null);
            case 3:
                return OrderedFragment.newInstance("CANCELLED", null);
            case 4:
                return OrderedFragment.newInstance("RETURNED", null);
            default:
                return OrderedFragment.newInstance("ALL", null);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}