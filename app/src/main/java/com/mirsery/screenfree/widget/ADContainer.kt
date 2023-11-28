package com.mirsery.screenfree.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
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

    private fun playImg(path: String) : Long{
        updateUI {
            stopVideo()
            imgView.visibility = LinearLayout.VISIBLE
            videoView.visibility = LinearLayout.GONE
            imgView.setImageBitmap(BitmapFactory.decodeFile(path))
        }
        return 5L
    }

    private fun playVideo(path: String) : Long{
        updateUI {
            stopVideo()
            imgView.visibility = LinearLayout.GONE
            videoView.visibility = LinearLayout.VISIBLE
            videoView.setVideoPath(path)
            videoView.start()
        }
        return videoView.duration.toLong()
    }

    fun playProgram(program: SimpleProgram):Long{
        // 0 img 1 video
        var time = 5L
        when (program.type) {
            0 -> {
              time =  playImg(program.path)
            }
            1 -> {
               time = playVideo(program.path)
            }
        }
        return time
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