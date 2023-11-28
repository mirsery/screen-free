package com.mirsery.screenfree.widget.complex

import android.content.Context
import android.widget.LinearLayout
import com.mirsery.screenfree.widget.container.StandADContainer

class ComplexADWidget(context: Context) : LinearLayout(context) {

    private var control: ComplexControl

    init {
        control = ComplexControl(this, StandADContainer(context), StandADContainer(context))
    }


    fun play() {
        control.startPlayer()
    }


    fun stop() {
        control.stop()
    }

}