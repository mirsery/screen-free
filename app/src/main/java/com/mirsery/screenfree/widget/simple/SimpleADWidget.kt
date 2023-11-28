package com.mirsery.screenfree.widget.simple

import android.content.Context
import com.mirsery.screenfree.widget.container.StandADContainer

/**
 * simple 模板框架
 */
class SimpleADWidget(context: Context) {

    private var control:SimpleControl

    var container: StandADContainer

    init {
        container = StandADContainer(context)
        control = SimpleControl()
        control.registerContainer(container)
    }


    fun play() {
        control.startSimplePlayer()
    }


    fun stop() {
        control.stop()
    }



}