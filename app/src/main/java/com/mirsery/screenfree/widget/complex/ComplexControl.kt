package com.mirsery.screenfree.widget.complex

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mirsery.screenfree.widget.container.StandADContainer
import com.mirsery.screenfree.widget.playerList.ComplexPlayerList
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
            stopAD()
            adWidget.removeAllViews()
            adWidget.orientation = LinearLayout.VERTICAL
            adWidget.addView(
                containerA, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private fun render(program: StandProgram) {
        updateUI {
            stopAD()
            adWidget.removeAllViews()
            when (program.theme) {
                ComplexType.SingleTemplate.toString() -> {
                    adWidget.orientation = LinearLayout.HORIZONTAL
                    adWidget.addView(containerA, LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    ))
                    program.program["A"]?.let { containerA.playProgram(it) }
                }

                ComplexType.SingleVerticalTemplate.toString() -> {
                    adWidget.orientation = LinearLayout.VERTICAL
                    adWidget.addView(containerA, LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    ))
                    program.program["A"]?.let { containerA.playProgram(it) }
                }

                ComplexType.ORow2ColTemplate.toString() -> {
                    adWidget.orientation = LinearLayout.VERTICAL
                    adWidget.addView(
                        containerA, LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, 0,
                            1F
                        )
                    )
                    adWidget.addView(
                        containerB, LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, 0,
                            1F
                        )
                    )
                    program.program["A"]?.let { containerA.playProgram(it) }
                    program.program["B"]?.let { containerB.playProgram(it) }
                }

                ComplexType.TRowOColTemplate.toString() -> {
                    adWidget.orientation = LinearLayout.HORIZONTAL
                    adWidget.addView(
                        containerA, LinearLayout.LayoutParams(
                            0, ViewGroup.LayoutParams.MATCH_PARENT,
                            1F
                        )
                    )
                    adWidget.addView(
                        containerB, LinearLayout.LayoutParams(
                            0, ViewGroup.LayoutParams.MATCH_PARENT,
                            1F
                        )
                    )
                    program.program["A"]?.let { containerA.playProgram(it) }
                    program.program["B"]?.let { containerB.playProgram(it) }
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
            delayTask(program.duration) { startPlayer() }
        }
    }


    private fun delayTask(time: Long = 30, play: () -> Unit) {
        executorService.schedule({
            play()
        }, time, TimeUnit.SECONDS)
    }

    private fun stopAD() {
        containerA.stopAndReleaseResources()
        containerB.stopAndReleaseResources()
    }


    fun stop() {
        executorService.shutdown()
        containerA.stopAndReleaseResources()
        containerB.stopAndReleaseResources()
        containerA = null !!
        containerB = null !!
    }
}