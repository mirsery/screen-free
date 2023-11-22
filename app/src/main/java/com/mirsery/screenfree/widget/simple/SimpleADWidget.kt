package com.mirsery.screenfree.widget.simple

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import com.mirsery.screenfree.R
import com.mirsery.screenfree.widget.FullScreenVideoView
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * only support png/jpg or mp4
 */
class SimpleADWidget(context: Context) : LinearLayout(context) {

    private var imgView: ImageView = ImageView(this.context)

    private var videoView: VideoView = FullScreenVideoView(this.context)

    private var executorService:ScheduledExecutorService =  Executors.newScheduledThreadPool(1);

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
                    startPlayList()
                    true
                }

                setOnCompletionListener {
                    //循环播放
                    videoView.setBackgroundResource(R.mipmap.bg)
                    startPlayList()
                }

                setOnPreparedListener { mp ->
                    Log.i("player", "onPrepared")
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


    private fun playImg() {
        stopVideo()
        viewControl(0)
        imgView.setImageResource(R.mipmap.admilk)
        delayTask()
    }

    private fun playVideo(path: String) {
        stopVideo()
        viewControl(1)
        videoView.setVideoPath(path)
        videoView.start()
    }

    private fun playImg(path: String) {
        stopVideo()
        viewControl(0)
        imgView.setImageBitmap(BitmapFactory.decodeFile(path))
        delayTask(5)
    }


    private fun stopVideo() {
        if (videoView.isPlaying) {
            videoView.stopPlayback()
        }
    }

    fun startPlayList() {
        val simpleProgram = SimplePlayerList.nextProgram()

        if (simpleProgram == null) {
            playImg()
        } else {
            when (simpleProgram.type) {
                0 -> {
                    playImg(simpleProgram.path)
                }
                1 -> {
                    playVideo(simpleProgram.path)
                }
            }
        }
    }

    private fun delayTask(time:Long=30){
        executorService.schedule({
            startPlayList()
        },time,TimeUnit.SECONDS)
    }



}