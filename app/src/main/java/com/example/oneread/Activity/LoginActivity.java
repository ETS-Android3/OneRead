package com.example.oneread.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.Adapter.ViewPagerAdapter;
import com.example.oneread.Fragment.LoginFragment;
import com.example.oneread.Fragment.RegisterFragment;
import com.example.oneread.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class LoginActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.viewpager)
    public ViewPager2 viewpager;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        //region main
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(LoginFragment.getInstance());
        viewPagerAdapter.addFragment(RegisterFragment.getInstance());
        viewpager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewpager, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText(R.string.login);
                    break;
                }
                case 1: {
                    tab.setText(R.string.register);
                    break;
                }
            }
        }).attach();
        //endregion
    }
}