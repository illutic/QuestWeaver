package g.sig.questweaver.remoteconfig

import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import g.sig.domain.repositories.RemoteConfigRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.hours

class RemoteConfigRepositoryImpl : RemoteConfigRepository {
    private inner class FetchRemoteConfigException : Exception()

    private val fetchAndActivateFlow = callbackFlow {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 3600 else 12.hours.inWholeSeconds
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                launch {
                    send(true)
                    close()
                }
            } else {
                launch {
                    send(false)
                    close(FetchRemoteConfigException())
                }
            }
        }
        awaitClose()
    }

    private fun exponentialBackoff(attempt: Long, exponent: Double = 2.0): Long =
        exponent.pow(attempt.toInt()).roundToLong() * 1000

    override suspend fun fetchRemoteConfig() {
        fetchAndActivateFlow.retryWhen { cause, attempt ->
            if (cause is FetchRemoteConfigException) {
                delay(exponentialBackoff(attempt))
                true
            } else {
                false
            }
        }.first()
    }

    override suspend fun getRemoteConfigValue(key: String): String =
        Firebase.remoteConfig.getString(key)
}