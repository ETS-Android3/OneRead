package com.example.oneread.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistorySearch {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "search")
    public String search;

    public HistorySearch(String search) {
        this.search = search;
    }

    public HistorySearch(){

    }
}
