package com.example.oneread.Helper;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import com.example.oneread.Model.Book;

import java.util.List;

public class BookDiffCallBack extends DiffUtil.Callback {
    private List<Book> oldList;
    private List<Book> newList;

    public BookDiffCallBack(List<Book> oldList, List<Book> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getEndpoint().equals(newList.get(newItemPosition).getEndpoint());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compareContent(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Book oldBook = oldList.get(oldItemPosition);
        Book newBook = newList.get(newItemPosition);
        Bundle bundle = new Bundle();

        if (!oldList.get(oldItemPosition).compareContent(newList.get(newItemPosition))) {
            bundle.putBoolean("changed", true);
        }
        return bundle;
    }
}
