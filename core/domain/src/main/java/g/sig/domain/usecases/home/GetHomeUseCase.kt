package g.sig.domain.usecases.home

import g.sig.domain.entities.Home
import g.sig.domain.repositories.RecentGamesRepository
import g.sig.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetHomeUseCase(
    private val recentGamesRepository: RecentGamesRepository,
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(): Home = withContext(mainDispatcher) {
        val user = userRepository.getUser()
        val recentGames = recentGamesRepository.getRecentGames()
        Home(user, recentGames)
    }
}