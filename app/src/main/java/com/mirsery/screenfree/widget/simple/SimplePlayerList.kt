package com.mirsery.screenfree.widget.simple

import android.os.Environment
import android.util.Log
import java.io.File

object SimplePlayerList {

    private val resourcePath = Environment.getExternalStorageDirectory().path + "/ScreenFree"


    private var playList : MutableList<SimpleProgram> = ArrayList()

    private var cur = 0

    init {
        val f = File(resourcePath)
        if(f.exists()){
            Log.i("simple", f.listFiles()?.size.toString())


            f.listFiles()?.forEach { file ->
                run {
                    if (file.path.endsWith(".png") || file.path.endsWith(".jpg")) {
                        playList.add( SimpleProgram(path = file.path, type = 0))
                    } else if (file.path.endsWith(".mp4")) {
                        playList.add( SimpleProgram(path = file.path, type = 1))
                    }
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