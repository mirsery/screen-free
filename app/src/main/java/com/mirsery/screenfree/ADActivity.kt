package com.mirsery.screenfree

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.mirsery.screenfree.widget.simple.SimpleADWidget


class ADActivity : AppCompatActivity() {

    private lateinit var adWidget: SimpleADWidget

    //    private lateinit var adWidget: ComplexADWidget
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customBottomUIMenu();
        setContentView(R.layout.activity_main)

        adWidget = SimpleADWidget(this)
//        adWidget = ComplexADWidget(this)
        addContentView(
            adWidget,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        adWidget.play()
    }

    override fun onDestroy() {
        adWidget.stop()
        adWidget = null!!
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        adWidget.stop()
        adWidget = null!!
        finish()
    }

    private fun customBottomUIMenu() {
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        } else {
            window.decorView.apply {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
    }
}