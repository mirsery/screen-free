package com.mirsery.screenfree.widget.container

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.mirsery.screenfree.R
import com.mirsery.screenfree.widget.program.SimpleProgram

/**
 * 广告容器
 * only support png/jpg or mp4
 * **/
class StandADContainer(context: Context) : FrameLayout(context) {

    private var imgView: ImageView = ImageView(context)

    private var videoView: VideoView = FullScreenVideoView(context)

    /**UI更新***/
    private val uiHandler: Handler = Handler(Looper.getMainLooper())


    init {
        addView(
            imgView.apply {
                scaleType = ImageView.ScaleType.FIT_XY
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                visibility = LinearLayout.VISIBLE
            })
        addView(
            videoView.apply {
                visibility = LinearLayout.GONE
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundResource(R.mipmap.bg)
                setOnErrorListener { _, _, _ ->
                    videoView.stopPlayback()
                    true
                }

                setOnCompletionListener {
                    videoView.setBackgroundResource(R.mipmap.bg)
                    videoView.resume()   //循环播放
                }

                setOnPreparedListener { mp ->
                    mp.setOnInfoListener { _, what, _ ->
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            videoView.setBackgroundColor(Color.TRANSPARENT)
                        }
                        true
                    }
                }
            })
    }

    private fun updateUI(action: () -> Unit) {
        uiHandler.post { action.invoke() }
    }

    private fun stopVideo() {
        if (videoView.isPlaying) {
            videoView.stopPlayback()
            videoView.suspend()
        }
    }

    private fun playImg(path: String) {
        updateUI {
            stopVideo()
            imgView.visibility = LinearLayout.VISIBLE
            videoView.visibility = LinearLayout.GONE
            Glide.with(context).load(path).into(imgView)
        }
    }

    private fun playVideo(path: String) {
        updateUI {
            stopVideo()
            imgView.visibility = LinearLayout.GONE
            videoView.visibility = LinearLayout.VISIBLE
            videoView.setVideoPath(path)
            videoView.start()
        }
    }

    fun playProgram(program: SimpleProgram) {
        // 0 img 1 video
        when (program.type) {
            0 -> {
                playImg(program.path)
            }

            1 -> {
                playVideo(program.path)
            }
        }
    }

    fun playProgram(program: SimpleProgram, callback: () -> Unit) {
        // 0 img 1 video
        when (program.type) {
            0 -> {
                playImg(program.path)
                callback()
            }

            1 -> {
                playVideo(program.path)
                videoView.setOnCompletionListener {
                    videoView.setBackgroundResource(R.mipmap.bg)
                    videoView.resume()   //循环播放
                    callback()
                }

                videoView.setOnErrorListener { _, _, _ ->
                    videoView.stopPlayback()
                    callback()
                    true
                }
            }
        }
    }

    fun stopAndReleaseResources() {
        updateUI {
            if (videoView.isPlaying) {
                videoView.stopPlayback()
                videoView.suspend()
            }
        }
    }

    fun hiddenALL() {
        updateUI {
            if (videoView.isPlaying) {
                videoView.stopPlayback()
                videoView.suspend()
            }
            imgView.visibility = LinearLayout.GONE
            videoView.visibility = LinearLayout.GONE
        }

    }


}