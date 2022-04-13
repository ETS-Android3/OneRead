package com.example.oneread.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.oneread.Common.Common;
import com.example.oneread.Common.SharedPrefs;
import com.example.oneread.Common.Utils;
import com.example.oneread.Model.User;
import com.example.oneread.R;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.avatar)
    RoundedImageView avatar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_logout)
    TextView btnLogout;

    private RoundedImageView avatarHeader;
    private TextView username;
    private Switch switchDarkMode;

    @OnClick(R.id.search)
    void openSearchActivity() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @OnClick(R.id.avatar)
    void openNavigationSidebar() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @OnClick(R.id.btn_login)
    void login() {
        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), Common.LOGIN_REQUEST_CODE);
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        if (Common.currentUser != null) {
            Common.currentUser = null;
            SharedPrefs.getInstance(this).remove(new String[]{Common.shareRefKeyUser, Common.shareRefKeyAccessToken});
        }
        avatar.setImageResource(R.drawable.avatar);
        avatarHeader.setImageResource(R.drawable.avatar);
        username.setText("");
        btnLogin.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadUserAccount();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        ButterKnife.bind(this);

        //region navigation menu
        navigationView.setNavigationItemSelectedListener(this);
        switchDarkMode = (Switch) navigationView.getMenu().findItem(R.id.dark_mode).getActionView();
        LinearLayout headerMenu = navigationView.getHeaderView(0).findViewById(R.id.header_menu);
        username = headerMenu.findViewById(R.id.username);
        avatarHeader = headerMenu.findViewById(R.id.avatar);
        avatarHeader.setOnClickListener(this);
        username.setOnClickListener(this);
        switchDarkMode.setOnClickListener(this);
        //endregion

        //region night mode
        int mode = SharedPrefs.getInstance(this).get(Common.shareRefKeyDarkMode, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(mode);
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            switchDarkMode.setChecked(true);
        } else switchDarkMode.setChecked(false);
        //endregion

        showUserAccount();
    }

    @Override
    public void onClick(View view) {
        if (view == username || view == avatarHeader) {
            Utils.showToast(this, "Not Available", Toast.LENGTH_SHORT);
        } else if (view == switchDarkMode) {
            switchToDarkMode();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_me: {
                Utils.showToast(this, "Not Available", Toast.LENGTH_SHORT);
                break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.LOGIN_REQUEST_CODE
            && resultCode == RESULT_OK) {
            loadUserAccount();
            showUserAccount();
        }
    }

    private void switchToDarkMode() {
        int state = AppCompatDelegate.getDefaultNightMode();
        if (state == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (state == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        state = AppCompatDelegate.getDefaultNightMode();
        SharedPrefs.getInstance(this).put(Common.shareRefKeyDarkMode, state);
    }

    private void showUserAccount() {
        if (Common.currentUser != null) {
            Picasso.get().load(Common.currentUser.getAvatar()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(avatar);
            Picasso.get().load(Common.currentUser.getAvatar()).placeholder(R.drawable.image_loading).error(R.drawable.image_err).into(avatarHeader);
            username.setText(Common.currentUser.getUsername());
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void loadUserAccount() {
        Common.currentUser = SharedPrefs.getInstance(this).get(Common.shareRefKeyUser, User.class, null);
        if (Common.currentUser != null) Common.currentUser.setAccessToken(SharedPrefs.getInstance(this).get(Common.shareRefKeyAccessToken, ""));
    }
}