package com.example.oneread.ui.main;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.example.oneread.ui.base.BaseActivity;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

public class MainActivity  extends BaseActivity implements MainContract.View {

    @Inject
    MainPresenter<MainContract.View> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setup();
    }

    @Override
    protected void setup() {

    }


}