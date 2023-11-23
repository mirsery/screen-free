package com.mirsery.screenfree.widget.simple

import android.os.Environment
import android.util.Log
import java.io.File

object SimplePlayerList {

    private val resourcePath =
        Environment.getExternalStorageDirectory().absolutePath + "/ScreenFree"


    private var playList: MutableList<SimpleProgram> = ArrayList()

    private var cur = 0

    init {
        this.refreshPlayList()
    }

    private fun refreshPlayList() {
        playList.clear()
        val f = File(resourcePath)

        if (!f.exists()) {
            f.mkdir()
        }
        f.listFiles()?.forEach {
            if (it.path.endsWith(".png") || it.path.endsWith(".jpg")) {
                playList.add(SimpleProgram(path = it.path, type = 0))
            } else if (it.path.endsWith(".mp4")) {
                playList.add(SimpleProgram(path = it.path, type = 1))
            }
        }
    }

    fun nextProgram(): SimpleProgram? {
//        Log.i("simple_player", "cur is $cur")
        if (playList.size < 1) {
            this.refreshPlayList()
            return null
        }

        if (cur >= playList.size) {
            this.refreshPlayList()
            cur = 0
        }
        cur++
        return playList[cur - 1]
    }

}