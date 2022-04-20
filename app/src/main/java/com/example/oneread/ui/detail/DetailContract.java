package com.example.oneread.ui.detail;

import com.example.oneread.data.network.model.Book;
import com.example.oneread.ui.base.BaseContract;

public interface DetailContract {
    interface View extends BaseContract.View {

        void setBook(Book book);
    }

    interface Presenter <V extends View> extends BaseContract.Presenter <V> {

        void getOnlineBook(String bookEndpoint);

        void getOfflineBook(String bookEndpoint);

    }
}
