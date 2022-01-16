package com.example.oneread.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.oneread.Fragment.LoginFragment;
import com.example.oneread.Fragment.RegisterFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return LoginFragment.getInstance();
            case 1:
                return RegisterFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
