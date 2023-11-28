package com.mirsery.screenfree.widget.playerList

import android.os.Environment
import com.mirsery.screenfree.widget.program.SimpleProgram
import java.io.File

object SimplePlayerList {

    private val resourcePath =
        Environment.getExternalStorageDirectory().absolutePath + "/ScreenFree"


    private var playList: MutableList<SimpleProgram> = ArrayList()

    private var cur = 0

    init {
        refreshPlayList()
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
            refreshPlayList()
            return null
        }

        if (cur >= playList.size) {
            refreshPlayList()
            cur = 0
        }
        cur++
        return playList[cur - 1]
    }

}