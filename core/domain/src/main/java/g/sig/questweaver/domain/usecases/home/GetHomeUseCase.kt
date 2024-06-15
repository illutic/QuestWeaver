package g.sig.questweaver.domain.usecases.home

import g.sig.questweaver.domain.entities.common.Home
import g.sig.questweaver.domain.repositories.NearbyRepository
import g.sig.questweaver.domain.repositories.UserRepository
import g.sig.questweaver.domain.usecases.permissions.GetNearbyPermissionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetHomeUseCase(
    private val nearbyPermissions: GetNearbyPermissionUseCase,
    private val nearbyRepository: NearbyRepository,
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Home = withContext(mainDispatcher) {
        val user = userRepository.getUser()
        val recentGames = nearbyRepository.getRecentGames()
        val permissions = nearbyPermissions()
        Home(user, recentGames, permissions)
    }
}
