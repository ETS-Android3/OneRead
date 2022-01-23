package com.example.oneread.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.oneread.Adapter.ViewPagerAdapter;
import com.example.oneread.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import io.reactivex.disposables.CompositeDisposable;

public class RankFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;
    private static RankFragment instance;
    private ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.viewpager)
    public ViewPager2 viewpager;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;

    public RankFragment() {
    }


    public static RankFragment getInstance() {
        if (instance == null) {
            return instance = new RankFragment();
        } else return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(TopDayFragment.getInstance());
        viewPagerAdapter.addFragment(TopMonthFragment.getInstance());
        viewPagerAdapter.addFragment(TopYearFragment.getInstance());
        viewpager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewpager, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText(R.string.top_day);
                    break;
                }
                case 1: {
                    tab.setText(R.string.top_month);
                    break;
                }
                case 2: {
                    tab.setText(R.string.top_year);
                    break;
                }
            }
        }).attach();
    }
}