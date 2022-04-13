package com.example.oneread.Common;

import android.widget.Toast;
import com.example.oneread.Model.User;
import com.example.oneread.Retrofit.IServiceAPI;
import com.example.oneread.Retrofit.RetrofitClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Common {
    // riêng
    public static User currentUser;
    public static String translateText = "";



    // chung
    public static Toast toast;
    public static IServiceAPI iServiceAPI = RetrofitClient.getInstance().create(IServiceAPI.class);
    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static final int MY_MICRO_REQUEST_CODE = 4;
    public static final int MY_CAMERA_REQUEST_CODE = 2;
    public static final int MY_RESULT_LOAD_IMAGE = 3;
    public static final int PICK_FILES = 5;
    public static final int LOGIN_REQUEST_CODE = 1;

    public static final String avatarFirebasePath = "avatar/";
    public static final String commentFirebasePath = "comment/";
    public static final String baseUrl = "http://192.168.1.11:3000/";
    public static final String shareRef = "SharedPrefs";
    public static final String shareRefKeyDarkMode = "NightModeInt";
    public static final String shareRefKeyUsername = "Username";
    public static final String shareRefKeyPassword = "Password";
    public static final String shareRefKeyAccessToken = "AccessToken";
    public static final String shareRefKeyRefreshToken = "RefreshToken";
    public static final String shareRefKeyUser = "User";
}
