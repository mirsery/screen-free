package com.mirsery.screenfree.widget.simple

import com.mirsery.screenfree.widget.ADContainer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SimpleControl {

    private var executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    private var container: ADContainer? = null
    fun registerContainer(adContainer: ADContainer) {
        container = adContainer
    }

    fun startSimplePlayer() {
        val simpleProgram = SimplePlayerList.nextProgram()
        if (simpleProgram == null) {
            delayTask(30) { startSimplePlayer() }
        } else {
            delayTask(container?.playProgram(simpleProgram) ?: 30) { startSimplePlayer() }
        }
    }


    private fun delayTask(time: Long = 30, play: () -> Unit) {
        executorService.schedule({
            play()
        }, time, TimeUnit.SECONDS)
    }
}