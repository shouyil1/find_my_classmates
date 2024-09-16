package com.example.findMyClassmates;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


        setupInboxButton();
        setupViewPager();
    }

    private  void setupInboxButton() {
        FloatingActionButton inboxButton = findViewById(R.id.inboxButton);
        inboxButton.setOnClickListener(v -> {
            Intent inboxIntent = new Intent(MainActivity.this, InboxActivity.class);
            startActivity(inboxIntent);
        });

    }

    private void setupViewPager() {
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.addFragment(new ClassesFragment(userEmail), "Classes");
        viewPagerAdapter.addFragment(new EnrollmentFragment(userEmail), "My Enrollment");
        viewPagerAdapter.addFragment(new ProfileFragment(userEmail), "Profile");
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position))
        ).attach();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void navigateToTab(int tabIndex) {
        if (viewPager != null && viewPagerAdapter != null && tabIndex >= 0 && tabIndex < viewPagerAdapter.getItemCount()) {
            viewPager.setCurrentItem(tabIndex);
//            viewPagerAdapter.notifyDataSetChanged(); // Refresh the ViewPager
        }
    }
}