package g.sig.questweaver.domain.usecases.remoteconfig

import g.sig.questweaver.domain.repositories.RemoteConfigRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FetchRemoteConfigValueUseCase(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(key: String) =
        withContext(mainDispatcher) {
            remoteConfigRepository.getRemoteConfigValue(key)
        }
}
