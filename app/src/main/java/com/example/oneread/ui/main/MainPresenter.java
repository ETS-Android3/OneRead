package com.example.oneread.ui.main;

import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.oneread.data.DataManager;
import com.example.oneread.ui.base.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.Arrays;


public class MainPresenter<V extends MainContract.View> extends BasePresenter<V>
        implements MainContract.Presenter<V> {

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
