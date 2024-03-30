package g.sig.domain.repositories

interface RemoteConfigRepository {
    suspend fun fetchRemoteConfig()
    suspend fun getRemoteConfigValue(key: String): String
}