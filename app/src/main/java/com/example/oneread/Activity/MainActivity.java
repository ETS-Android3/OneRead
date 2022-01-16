package com.example.oneread.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.oneread.R;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.navigation_view)
    public NavigationView navigationView;

    private LinearLayout headerMenu;
    private RoundedImageView img_avatar;
    private TextView username;
    private Switch btn_dark_mode;


    int NightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        btn_dark_mode = (Switch) navigationView.getMenu().findItem(R.id.dark_mode).getActionView();
        headerMenu = navigationView.getHeaderView(0).findViewById(R.id.header_menu);
        img_avatar = headerMenu.findViewById(R.id.img_avatar);
        username = headerMenu.findViewById(R.id.username);
        img_avatar.setOnClickListener(this);
        username.setOnClickListener(this);
        btn_dark_mode.setOnClickListener(this);
        //endregion

        //region night mode
        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        NightMode = sharedPreferences.getInt("NightModeInt", 1);
        AppCompatDelegate.setDefaultNightMode(NightMode);
        if (NightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            btn_dark_mode.setChecked(true);
        } else btn_dark_mode.setChecked(false);
        //endregion
    }

    @Override
    public void onClick(View view) {
        if (view == username || view == img_avatar) {
            return;
        }
        if (view == btn_dark_mode) {
            switchToDarkMode();
            return;
        }
    }

    private void switchToDarkMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        NightMode = AppCompatDelegate.getDefaultNightMode();

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt("NightModeInt", NightMode);
        editor.apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_me: {
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.login: {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            }
        }
        return false;
    }
}