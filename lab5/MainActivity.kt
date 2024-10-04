import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe


class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null
    private var _gravity = MutableLiveData(SensorData(0f, 0f))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarbleApp(gravity = _gravity)
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        gravitySensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = event.values[0]
            val y = event.values[1]
            _gravity.value = SensorData(x, y)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

@Composable
fun MarbleApp(gravity: LiveData<SensorData>) {
    val gravityValue by gravity.observeAsState(SensorData(0f, 0f))

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val marbleSize = 50.dp
        val xPos = (gravityValue.x / 9.8f) * maxWidth.value.toInt() / 2
        val yPos = -(gravityValue.y / 9.8f) * maxHeight.value.toInt() / 2

        Box(
            modifier = Modifier
                .offset { IntOffset(xPos.toInt(), yPos.toInt()) }
                .size(marbleSize)
                .background(Color.Red, shape = CircleShape)
        )
    }
}