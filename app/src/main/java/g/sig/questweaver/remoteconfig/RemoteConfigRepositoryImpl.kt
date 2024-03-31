package g.sig.questweaver.remoteconfig

import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import g.sig.domain.repositories.RemoteConfigRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.hours

class RemoteConfigRepositoryImpl : RemoteConfigRepository {
    private suspend fun fetchAndActivateRemoteConfig() = suspendCancellableCoroutine { continuation ->
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 3600 else 12.hours.inWholeSeconds
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            continuation.resume(task.isSuccessful)
        }
    }

    private fun exponentialBackoff(attempt: Int, exponent: Double = 2.0): Long =
        exponent.pow(attempt).roundToLong() * 1000

    override suspend fun fetchRemoteConfig() {
        var retryCount = 0
        do {
            val activatedRemoteConfig = fetchAndActivateRemoteConfig()
            delay(exponentialBackoff(retryCount))
            retryCount++
        } while (!activatedRemoteConfig)
    }

    override suspend fun getRemoteConfigValue(key: String): String =
        Firebase.remoteConfig.getString(key)
}