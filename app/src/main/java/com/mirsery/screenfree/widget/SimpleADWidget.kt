package com.mirsery.screenfree.widget

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import com.mirsery.screenfree.R

class SimpleADWidget(context: Context) : LinearLayout(context) {

    private var imgView: ImageView = ImageView(this.context)

    private var videoView: VideoView = FullScreenVideoView(this.context)


    init {
        addView(
            imgView.apply {
                scaleType = ImageView.ScaleType.FIT_XY
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                visibility = VISIBLE
            })
        addView(
            videoView.apply {
                visibility = GONE
                setBackgroundResource(R.mipmap.bg)
                setOnErrorListener { _, what, _ ->
                    Log.e("player", what.toString())
                    videoView.stopPlayback()
                    defaultShow()
                    true
                }

                setOnCompletionListener {
                    //循环播放
                    videoView.setBackgroundResource(R.mipmap.bg)
                    playVideo()
                }

                setOnPreparedListener { mp ->
                    Log.i("player", "onPrepared");
                    mp.setOnInfoListener { _, what, _ ->
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            videoView.setBackgroundColor(Color.TRANSPARENT)
                        }
                        true
                    }
                }
            })
    }

    private fun viewControl(i: Int = 0) {
        // 0 img 1 video
        when (i) {
            0 -> {
                imgView.visibility = VISIBLE
                videoView.visibility = GONE
            }

            1 -> {
                imgView.visibility = GONE
                videoView.visibility = VISIBLE
            }
        }
    }


    private fun defaultShow() {
        viewControl(0)
        imgView.setImageResource(R.mipmap.admilk)
    }

    fun playVideo() {
        viewControl(1)
        if (videoView.isPlaying) {
            videoView.pause()
        }
        videoView.setVideoPath("http://oss.hytiot.com/hytad1/afacsgo.mp4")
        videoView.start()
    }


    fun stopVideo() {
        if (videoView.isPlaying) {
            videoView.stopPlayback()
        }
    }

}