package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null
    private val _gravity = MutableStateFlow(SensorData(0f, 0f))
    val gravity: StateFlow<SensorData> = _gravity.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarbleApp(gravity = gravity)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        gravitySensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
//            Log.d("SensorData", "X: $x, Y: $y")
            _gravity.value = SensorData(x, y)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

data class SensorData(val x: Float, val y: Float)

@Composable
fun MarbleApp(gravity: StateFlow<SensorData>) {
    val gravityValue by gravity.collectAsState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxX = maxWidth.value
        val maxY = maxHeight.value

        val xOffset by remember(gravityValue) {
            derivedStateOf { (gravityValue.x / 9.8f) * (maxX / 2) }
        }
        val yOffset by remember(gravityValue) {
            derivedStateOf { -(gravityValue.y / 9.8f) * (maxY / 2) }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(xOffset.toInt(), yOffset.toInt()) }
                .size(50.dp)
                .background(Color.Blue, shape = CircleShape)
        )
    }
}