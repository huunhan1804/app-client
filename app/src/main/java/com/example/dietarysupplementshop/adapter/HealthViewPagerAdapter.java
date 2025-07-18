package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dietarysupplementshop.HealthArticlesFragment;

public class HealthViewPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 2;

    public HealthViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HealthArticlesFragment();
            case 1:
                return new Fragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}