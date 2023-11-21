package com.mirsery.screenfree.widget

import android.content.Context
import android.widget.VideoView

class FullScreenVideoView(context: Context) : VideoView(context){

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(0, widthMeasureSpec)
        val height = getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }
}