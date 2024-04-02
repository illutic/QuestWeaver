package g.sig.data.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object BatteryLevelReceiver : BroadcastReceiver() {
    private val _level = MutableStateFlow(BatteryLevel.Okay)
    val level = _level.asStateFlow()

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BATTERY_LOW -> _level.value = BatteryLevel.Low
            Intent.ACTION_BATTERY_OKAY -> _level.value = BatteryLevel.Okay
        }
    }
}