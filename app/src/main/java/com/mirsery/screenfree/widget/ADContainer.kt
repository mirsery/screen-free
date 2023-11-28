package com.mirsery.screenfree.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import com.mirsery.screenfree.R
import com.mirsery.screenfree.widget.simple.SimpleProgram

class ADContainer(context: Context) : FrameLayout(context) {

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
                setBackgroundResource(R.mipmap.bg)
                setOnErrorListener { _, what, _ ->
                    Log.e("player", what.toString())
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

    private fun viewControl(i: Int = 0) {
        // 0 img 1 video
        when (i) {
            0 -> {
                imgView.visibility = LinearLayout.VISIBLE
                videoView.visibility = LinearLayout.GONE
            }

            1 -> {
                imgView.visibility = LinearLayout.GONE
                videoView.visibility = LinearLayout.VISIBLE
            }
        }
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
            viewControl(0)
            imgView.setImageBitmap(BitmapFactory.decodeFile(path))
        }
    }

    private fun playVideo(path: String) {
        updateUI {
            stopVideo()
            viewControl(1)
            videoView.setVideoPath(path)
            videoView.start()
        }
    }

    fun playProgram(program: SimpleProgram) {
        when (program.type) {
            0 -> {
                playImg(program.path)
            }

            1 -> {
                playVideo(program.path)
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


}