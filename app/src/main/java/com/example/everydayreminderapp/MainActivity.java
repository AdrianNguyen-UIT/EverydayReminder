package com.example.everydayreminderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.everydayreminderapp.Adapter.MainAdapter;
import com.example.everydayreminderapp.Database.DatabaseHelper;
import com.example.everydayreminderapp.Model.EventModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.MainTabLayout);
        viewPager = findViewById(R.id.MainViewPager);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(getResources().getString(R.string.TodayTabText));
        arrayList.add(getResources().getString(R.string.PlansTabText));

        prepareViewPager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_today_black_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.baseline_calendar_view_day_black_48dp);

        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabSelectedColor), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabSelectedColor), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabUnselectedColor), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }

        });

        tabLayout.getTabAt(0).select();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        TodayEventsFragment todayEventsFragment = new TodayEventsFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("title", arrayList.get(0));
        todayEventsFragment.setArguments(bundle1);
        adapter.AddFragment(todayEventsFragment, arrayList.get(0));

        PlansFragment plansFragment = new PlansFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("title", arrayList.get(1));
        plansFragment.setArguments(bundle2);
        adapter.AddFragment(plansFragment, arrayList.get(1));

        todayEventsFragment.RegisterOnCreateAddEventDialogListener(plansFragment);

        viewPager.setAdapter(adapter);
    }
}