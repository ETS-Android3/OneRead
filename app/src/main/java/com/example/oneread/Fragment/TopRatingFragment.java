package com.example.oneread.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.oneread.R;
import io.reactivex.disposables.CompositeDisposable;

public class TopRatingFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;
    private static TopRatingFragment instance;

    public TopRatingFragment() {
    }


    public static TopRatingFragment getInstance() {
        if (instance == null) {
            return instance = new TopRatingFragment();
        } else return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        compositeDisposable.dispose();
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
        View view = inflater.inflate(R.layout.fragment_top_rating, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();
    }
}