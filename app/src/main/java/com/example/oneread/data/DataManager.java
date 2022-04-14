package com.example.oneread.data;

import android.os.Environment;
import androidx.lifecycle.LiveData;
import com.example.oneread.data.db.DBHelper;
import com.example.oneread.data.file.FileHelper;
import com.example.oneread.data.network.ApiHelper;
import com.example.oneread.data.prefs.PrefsHelper;
import com.example.oneread.utils.AppConstants;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;

@Singleton
public class DataManager {
    private static final String TAG = "DataManager";
    private final PrefsHelper prefsHelper;
    private final ApiHelper apiHelper;
    private final DBHelper dbHelper;
    private final FileHelper fileHelper;

    @Inject
    public DataManager(PrefsHelper prefsHelper,
                       ApiHelper apiHelper, DBHelper dbHelper, FileHelper fileHelper) {
        this.prefsHelper = prefsHelper;
        this.apiHelper = apiHelper;
        this.dbHelper = dbHelper;
        this.fileHelper = fileHelper;
    }

    public List<String> downloadImages(List<String> imageURLs, String path) {
         return fileHelper.downloadImages(imageURLs, path);
    }

    public List<String> downloadTexts(List<String> texts, String path) {
        return fileHelper.downloadTexts(texts, path);
    }

}
