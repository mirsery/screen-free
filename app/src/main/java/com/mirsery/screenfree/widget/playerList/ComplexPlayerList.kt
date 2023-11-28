package com.mirsery.screenfree.widget.playerList

import android.os.Environment
import android.util.Log
import com.mirsery.screenfree.widget.complex.ComplexType
import com.mirsery.screenfree.widget.program.SimpleProgram
import com.mirsery.screenfree.widget.program.StandProgram
import java.io.File

object  ComplexPlayerList {

    private val resourcePath =
        Environment.getExternalStorageDirectory().absolutePath + "/ScreenFree"


    private var playList: MutableList<StandProgram> = ArrayList()

    private var cur = 0

    init {
        refreshPlayList()
        Log.i("simple_player", "resource is $resourcePath")

    }

    private fun refreshPlayList() {
        playList.clear()
        val f = File(resourcePath)

        if (!f.exists()) {
            f.mkdir()
        }


        f.listFiles()?.forEach {
            if (it.path.endsWith(".png") || it.path.endsWith(".jpg")) {

                val map :MutableMap<String,SimpleProgram> = HashMap()
                map["A"] = SimpleProgram(path = it.path, type = 0)
                map["B"] = SimpleProgram(path = it.path, type = 0)

                playList.add(StandProgram(ComplexType.ORow2ColTemplate.toString(),5L,map))

            } else if (it.path.endsWith(".mp4")) {
                val map :MutableMap<String,SimpleProgram> = HashMap()
                map["A"] = SimpleProgram(path = it.path, type = 1)
                playList.add(StandProgram(ComplexType.SingleVerticalTemplate.toString(),40L,map))
            }
        }
    }

    fun nextProgram(): StandProgram? {

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

