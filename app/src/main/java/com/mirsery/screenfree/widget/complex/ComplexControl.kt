package com.mirsery.screenfree.widget.complex

import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import com.mirsery.screenfree.widget.container.StandADContainer
import com.mirsery.screenfree.widget.program.StandProgram
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ComplexControl(
    adWidget: ComplexADWidget,
    containerA: StandADContainer,
    containerB: StandADContainer,
) {
    private var executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    private var containerA: StandADContainer

    private var containerB: StandADContainer

    private var adWidget: ComplexADWidget

    /**UI更新***/
    private val uiHandler: Handler = Handler(Looper.getMainLooper())

    init {
        this.adWidget = adWidget
        this.containerA = containerA
        this.containerB = containerB
    }

    private fun updateUI(action: () -> Unit) {
        uiHandler.post { action.invoke() }
    }

    private fun render() {
        updateUI {
            adWidget.removeAllViews()
            stopAB()
            adWidget.orientation = LinearLayout.HORIZONTAL
            adWidget.addView(containerA)
        }
    }

    private fun render(program: StandProgram) {
        updateUI {
            adWidget.removeAllViews()
            stopAB()
            when (program.theme) {
                "singleTemplate" -> {
                    adWidget.orientation = LinearLayout.HORIZONTAL
                    adWidget.addView(containerA)
                    program.program["A"]?.let {
                        if (it.type == 1) {
                            containerA.playProgram(it) { startPlayer() }
                        } else {
                            containerA.playProgram(it)
                            delayTask(5) { startPlayer() }
                        }
                    }
                }

                "SingleVerticalTemplate" -> {
                    adWidget.orientation = LinearLayout.VERTICAL
                    adWidget.addView(containerA)
                    program.program["A"]?.let {
                        containerA.playProgram(it)
                        delayTask(5) { startPlayer() }
                    }
                }

                "ORow2ColTemplate" -> {
                    adWidget.orientation = LinearLayout.VERTICAL
                    adWidget.addView(containerA)
                    adWidget.addView(containerB)
                    program.program["A"]?.let { containerA.playProgram(it) }
                    program.program["B"]?.let { containerB.playProgram(it) }
                    delayTask(5) { startPlayer() }
                }

                "TRowOColTemplate" -> {
                    adWidget.orientation = LinearLayout.HORIZONTAL
                    adWidget.addView(containerA)
                    adWidget.addView(containerB)
                    program.program["A"]?.let { containerA.playProgram(it) }
                    program.program["B"]?.let { containerB.playProgram(it) }
                    delayTask(5) { startPlayer() }
                }
            }
        }

    }

    fun startPlayer() {
        val program = ComplexPlayerList.nextProgram()
        if (program == null) {
            render()
            delayTask(30) { startPlayer() }
        } else {
            render(program)
        }
    }


    private fun delayTask(time: Long = 30, play: () -> Unit) {
        executorService.schedule({
            play()
        }, time, TimeUnit.SECONDS)
    }

    private fun stopAB() {
        containerA.stopAndReleaseResources()
        containerB.stopAndReleaseResources()
    }


    fun stop() {
        executorService.shutdown()
        containerA.stopAndReleaseResources()
        containerB.stopAndReleaseResources()
    }
}