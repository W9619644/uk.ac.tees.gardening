package com.example.greenthumb.common

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(private val context: Context, private val onShake: () -> Unit) :
    SensorEventListener {

    private val accelerationThreshold = 15.0f // Adjust this value to set the sensitivity

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometerSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    init {
        requireNotNull(accelerometerSensor) { "Accelerometer sensor not found" }
    }

    fun start() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val acceleration = sqrt(x * x + y * y + z * z)

        if (acceleration >= accelerationThreshold) {
            onShake.invoke()
        }
    }
}
