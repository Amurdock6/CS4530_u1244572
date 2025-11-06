package com.example.marblegame

import android.app.Application
import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// MVVM brain: simple physics integration with friction + bounds clamping.
class MarbleViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TiltRepository(app)
    private var collectJob: Job? = null

    data class UiState(
        val xPx: Float = 0f,
        val yPx: Float = 0f,
        val vx: Float = 0f,
        val vy: Float = 0f,
        val ballDiameterPx: Float = 80f,
        val maxX: Float = 0f,
        val maxY: Float = 0f
    )

    var ui by mutableStateOf(UiState())
        private set

    private val accelScale = 1000f    // g -> px/s^2
    private val friction = 0.985f     // 0..1; lower = more drag

    fun setBounds(containerWpx: Float, containerHpx: Float, ballDiameterPx: Float) {
        val newMaxX = (containerWpx - ballDiameterPx).coerceAtLeast(0f)
        val newMaxY = (containerHpx - ballDiameterPx).coerceAtLeast(0f)

        val nx = if (ui.xPx == 0f && ui.yPx == 0f) newMaxX / 2f else ui.xPx.coerceIn(0f, newMaxX)
        val ny = if (ui.xPx == 0f && ui.yPx == 0f) newMaxY / 2f else ui.yPx.coerceIn(0f, newMaxY)

        ui = ui.copy(
            xPx = nx, yPx = ny,
            maxX = newMaxX, maxY = newMaxY,
            ballDiameterPx = ballDiameterPx
        )
    }

    fun start() {
        repo.start()
        if (collectJob?.isActive == true) return

        collectJob = viewModelScope.launch {
            var lastT = SystemClock.elapsedRealtimeNanos()
            repo.gravityFlow.collectLatest { g ->
                val now = SystemClock.elapsedRealtimeNanos()
                val dt = ((now - lastT).coerceAtMost(300_000_000L)) / 1_000_000_000f // cap 0.3s
                lastT = now

                // Map & flip so tilting RIGHT moves RIGHT, tilting FORWARD moves DOWN.
                val ax = -g[0]
                val ay = g[1]

                var vx = (ui.vx + ax * accelScale * dt) * friction
                var vy = (ui.vy + ay * accelScale * dt) * friction

                var x = ui.xPx + vx * dt
                var y = ui.yPx + vy * dt

                if (x < 0f) { x = 0f; vx = 0f }
                if (y < 0f) { y = 0f; vy = 0f }
                if (x > ui.maxX) { x = ui.maxX; vx = 0f }
                if (y > ui.maxY) { y = ui.maxY; vy = 0f }

                ui = ui.copy(xPx = x, yPx = y, vx = vx, vy = vy)
            }
        }
    }

    fun stop() {
        repo.stop()
        collectJob?.cancel()
        collectJob = null
    }

    fun recenter() {
        ui = ui.copy(xPx = ui.maxX / 2f, yPx = ui.maxY / 2f, vx = 0f, vy = 0f)
    }

    fun positionRounded(): Pair<Int, Int> = ui.xPx.roundToInt() to ui.yPx.roundToInt()
}
