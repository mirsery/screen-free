package com.hytiot.demo;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;

import com.mirsery.open.widget.simple.SimpleADWidget;

public class MainActivity extends AppCompatActivity {

    private SimpleADWidget adWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customBottomUIMenu();
        setContentView(R.layout.activity_main);

        adWidget = findViewById(R.id.ad_view);
        adWidget.play();
    }

    @Override
    protected void onDestroy() {
        adWidget.stop();
        adWidget = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adWidget.stop();
        adWidget = null;

    }

    private void customBottomUIMenu() {
        getSupportActionBar().hide();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().hide(WindowInsetsCompat.Type.navigationBars());
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}