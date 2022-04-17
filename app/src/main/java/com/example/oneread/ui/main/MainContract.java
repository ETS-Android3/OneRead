package com.example.oneread.ui.main;

import com.example.oneread.data.network.model.User;
import com.example.oneread.ui.base.BaseContract;

public interface MainContract {
    interface View extends BaseContract.View {

        void openLoginActivity();

        void openSearchActivity();

        void openBookCaseActivity();

        void showAboutFragment();

        void updateUserProfile(User user);

        void setThemeMode(int mode);

        void openNavigationDrawer();

        void closeNavigationDrawer();
    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

        void onOptionAboutClick();

        void onOptionBookCaseClick();

        void onOptionLoginClick();

        void onOptionLogoutClick();

        void onUserLoggedIn();

        void onViewInitialized();

        void onNavMenuCreated();

        void switchThemeMode();

    }
}
