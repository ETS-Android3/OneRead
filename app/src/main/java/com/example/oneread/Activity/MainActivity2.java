package com.example.oneread.Activity;

import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.example.oneread.R;

public class MainActivity2 extends AppCompatActivity {

    RelativeLayout relativeLayout;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = findViewById(R.id.test);
        textView.getBackground().setAlpha(200);

    }
}