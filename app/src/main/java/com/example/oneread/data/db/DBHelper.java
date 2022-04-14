package com.example.oneread.data.db;

import com.example.oneread.di.anotation.FixedThreadPool;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Singleton
public class DBHelper {

    private ExecutorService executor;

    @Inject
    public DBHelper(@FixedThreadPool ExecutorService executor) {
        this.executor = executor;
    }
}
