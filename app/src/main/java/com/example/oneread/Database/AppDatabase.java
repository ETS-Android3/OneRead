package com.example.oneread.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.oneread.DAO.HistorySearchDao;
import com.example.oneread.Entity.HistorySearch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HistorySearch.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistorySearchDao historySearchDao();

    private static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 2;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "book_database")
                            .build();
                }
            }
        }
        return instance;
    }
}