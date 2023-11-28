package com.mirsery.screenfree.widget.simple

import com.mirsery.screenfree.widget.container.StandADContainer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * simple 播放控制器
 * **/
class SimpleControl {

    private var executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    private var container: StandADContainer? = null
    fun registerContainer(standAdContainer: StandADContainer) {
        container = standAdContainer
    }

    fun startSimplePlayer() {
        val simpleProgram = SimplePlayerList.nextProgram()
        if (simpleProgram == null) {
            delayTask(30) { startSimplePlayer() }
        } else if(simpleProgram.type == 0) {
            container?.playProgram(simpleProgram)
            delayTask(5){ startSimplePlayer() }
        } else {
            container?.playProgram(simpleProgram) { startSimplePlayer() }
        }
    }


    private fun delayTask(time: Long = 30, play: () -> Unit) {
        executorService.schedule({
            play()
        }, time, TimeUnit.SECONDS)
    }


    fun stop(){
        executorService.shutdown()
        container?.stopAndReleaseResources()
    }
}