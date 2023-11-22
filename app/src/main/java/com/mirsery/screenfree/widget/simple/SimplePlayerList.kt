package com.mirsery.screenfree.widget.simple

import android.os.Environment
import java.io.File

object SimplePlayerList {

    private val resourcePath = Environment.getExternalStorageDirectory().absolutePath + "/ScreenFree"


    private var playList : MutableList<SimpleProgram> = ArrayList()

    private var cur = 0

    init {
        val f = File(resourcePath)
        if(f.exists()){
            f.listFiles()?.forEach {
                if (it.path.endsWith(".png") || it.path.endsWith(".jpg")) {
                    playList.add( SimpleProgram(path = it.path, type = 0))
                } else if (it.path.endsWith(".mp4")) {
                    playList.add( SimpleProgram(path = it.path, type = 1))
                }
            }
        }
    }

    fun nextProgram() : SimpleProgram? {
        if(playList.size < 1)
            return null

        if( cur >= playList.size){
            cur = 0
        }
        cur ++
        return playList[cur - 1]
    }

}