package com.example.oneread.Activity;

import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.example.oneread.R;

public class MainActivity2 extends AppCompatActivity {

    RelativeLayout relativeLayout;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cardView = findViewById(R.id.select_country_main);
        cardView.getBackground().setAlpha(200);

    }
}