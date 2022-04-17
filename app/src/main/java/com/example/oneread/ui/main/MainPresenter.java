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

    @Override
    public void onOptionAboutClick() {
        getView().showAboutFragment();
        new Handler().postDelayed(() -> getView().closeNavigationDrawer(), 200);
    }

    @Override
    public void onOptionBookCaseClick() {
        getView().openBookCaseActivity();
        new Handler().postDelayed(() -> getView().closeNavigationDrawer(), 200);
    }

    @Override
    public void onOptionLoginClick() {
        getView().openLoginActivity();
    }

    @Override
    public void onOptionLogoutClick() {
        getDataManager().removeCurrentUser();
        getView().updateUserProfile(null);
    }

    @Override
    public void onUserLoggedIn() {
        getView().updateUserProfile(getDataManager().getCurrentUser());
    }

    @Override
    public void onViewInitialized() {

    }

    @Override
    public void onNavMenuCreated() {
        getView().updateUserProfile(getDataManager().getCurrentUser());
        getView().setThemeMode(getDataManager().getNightMode());
    }

    @Override
    public void switchThemeMode() {
        if (getDataManager().getNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            getDataManager().setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDataManager().setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        getView().setThemeMode(getDataManager().getNightMode());

    }
}
