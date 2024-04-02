package g.sig.data.repositories

import g.sig.data.datasources.nearby.NearbyGameDataSource
import g.sig.data.entities.Game.Companion.fromDomain
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Game
import g.sig.domain.entities.User
import g.sig.domain.repositories.NearbyGamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NearbyGamesRepositoryImpl(private val dataSource: NearbyGameDataSource) : NearbyGamesRepository {
    override fun findNearbyGames(): Flow<Game> = dataSource.findNearbyGames().map { it.toDomain() }

    override fun joinGame(user: User, game: Game): Flow<ConnectionState> = dataSource.joinGame(user.fromDomain(), game.fromDomain()).map { it.toDomain() }
}