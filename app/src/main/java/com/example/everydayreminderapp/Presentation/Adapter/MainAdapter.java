package com.example.everydayreminderapp.Presentation.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public MainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void AddFragment(Fragment fragment, String title) {
        arrayList.add(title);
        fragmentArrayList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayList.get(position);
    }
}
