package com.example.marblegame

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

// Handles SensorManager and exposes a smoothed gravity stream (XYZ) via Flow.
class TiltRepository(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    private val accelSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var active: Sensor? = null

    private val _gravityFlow = MutableSharedFlow<FloatArray>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val gravityFlow: SharedFlow<FloatArray> = _gravityFlow

    // Low-pass filter state
    private var gX = 0f
    private var gY = 0f
    private var gZ = 0f
    private val alpha = 0.90f // closer to 1 = smoother

    fun start() {
        active = gravitySensor ?: accelSensor
        active?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        active = null
    }

    override fun onSensorChanged(event: android.hardware.SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        gX = alpha * gX + (1f - alpha) * x
        gY = alpha * gY + (1f - alpha) * y
        gZ = alpha * gZ + (1f - alpha) * z

        _gravityFlow.tryEmit(floatArrayOf(gX, gY, gZ))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no-op */ }
}
