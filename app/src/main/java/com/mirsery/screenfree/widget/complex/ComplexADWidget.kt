package com.mirsery.screenfree.widget.complex

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mirsery.screenfree.widget.container.StandADContainer

class ComplexADWidget(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

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