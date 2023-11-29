package com.mirsery.screenfree.widget.simple

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mirsery.screenfree.widget.container.StandADContainer

/**
 * simple 模板框架
 */
class SimpleADWidget(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var control: SimpleControl

    var container: StandADContainer

    init {
        container = StandADContainer(context)
        control = SimpleControl()
        control.registerContainer(container)
        addView(
            container, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }


    fun play() {
        control.startSimplePlayer()
    }


    fun stop() {
        control.stop()
    }


}