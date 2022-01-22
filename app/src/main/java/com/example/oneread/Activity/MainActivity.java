package com.example.oneread.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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
import com.example.oneread.Fragment.SuggestForYouFragment;
import com.example.oneread.Listener.IUnauthorizedListener;
import com.example.oneread.Model.User;
import com.example.oneread.R;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, IUnauthorizedListener {

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

    private SuggestForYouFragment suggestFragment;
    private RoundedImageView avatarHeader;
    private TextView username;
    private Switch btn_dark_mode;

    @OnClick(R.id.card_search)
    void openSearchActivity() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @OnClick(R.id.avatar)
    void OnAvatarClick() {
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
            avatar.setImageResource(R.drawable.avatar);
            avatarHeader.setImageResource(R.drawable.avatar);
            username.setText("");
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
        suggestFragment.onLogout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {
        ButterKnife.bind(this);

        //region navigation menu
        navigationView.setNavigationItemSelectedListener(this);
        suggestFragment = (SuggestForYouFragment) getSupportFragmentManager().findFragmentById(R.id.suggest);
        btn_dark_mode = (Switch) navigationView.getMenu().findItem(R.id.dark_mode).getActionView();
        LinearLayout headerMenu = navigationView.getHeaderView(0).findViewById(R.id.header_menu);
        username = headerMenu.findViewById(R.id.username);
        avatarHeader = headerMenu.findViewById(R.id.avatar);
        avatarHeader.setOnClickListener(this);
        username.setOnClickListener(this);
        btn_dark_mode.setOnClickListener(this);
        //endregion

        //region night mode
        AppCompatDelegate.setDefaultNightMode(SharedPrefs.getInstance(this).get(Common.shareRefKeyDarkMode, AppCompatDelegate.MODE_NIGHT_NO));
        if (SharedPrefs.getInstance(this).get(Common.shareRefKeyDarkMode, AppCompatDelegate.MODE_NIGHT_NO) == AppCompatDelegate.MODE_NIGHT_YES) {
            btn_dark_mode.setChecked(true);
        } else btn_dark_mode.setChecked(false);
        //endregion

        checkLogin();
    }

    @Override
    public void onClick(View view) {
        if (view == username || view == avatarHeader) {
            Utils.showToast(this, "Not Available", Toast.LENGTH_SHORT);
            return;
        }
        if (view == btn_dark_mode) {
            switchToDarkMode();
            return;
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
            checkLogin();
            suggestFragment.onLogin();
        }
    }

    private void switchToDarkMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        int state = AppCompatDelegate.getDefaultNightMode();
        SharedPrefs.getInstance(this).put(Common.shareRefKeyDarkMode, state);
    }

    private void checkLogin() {
        Common.currentUser = SharedPrefs.getInstance(this).get(Common.shareRefKeyUser, User.class, null);
        if (Common.currentUser != null) {
            Common.currentUser.setAccessToken(SharedPrefs.getInstance(this).get(Common.shareRefKeyAccessToken, ""));
            Picasso.get().load(Common.currentUser.getAvatar()).into(avatar);
            Picasso.get().load(Common.currentUser.getAvatar()).into(avatarHeader);
            username.setText(Common.currentUser.getUsername());
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUnauthorized() {
        logout();
        login();
    }
}