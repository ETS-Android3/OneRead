package com.example.oneread.ui.main;

import com.example.oneread.ui.base.BaseContract;

public interface MainContract {
    interface View extends BaseContract.View {
    }

    interface Presenter <V extends View> extends BaseContract.Presenter<V> {

    }
}
