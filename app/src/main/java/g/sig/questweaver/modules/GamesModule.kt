package g.sig.questweaver.modules

import android.content.Context
import com.google.android.gms.nearby.connection.ConnectionsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.game.GameSessionDataSource
import g.sig.data.datasources.game.GameSessionDataSourceImpl
import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.nearby.NearbyDataSourceImpl
import g.sig.data.datasources.nearby.PayloadCallback
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.datasources.recentgames.RecentGamesLocalDataSource
import g.sig.data.repositories.NearbyRepositoryImpl
import g.sig.domain.repositories.NearbyRepository
import g.sig.domain.usecases.nearby.GetNearbyGamesUseCase
import g.sig.domain.usecases.nearby.JoinGameUseCase
import g.sig.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamesModule {

    @Provides
    @Singleton
    fun provideGameSessionDataSource(): GameSessionDataSource {
        return GameSessionDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideGetNearbyGamesUseCase(
        repository: NearbyRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): GetNearbyGamesUseCase {
        return GetNearbyGamesUseCase(repository, getUserUseCase, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideJoinGameUseCase(
        repository: NearbyRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): JoinGameUseCase {
        return JoinGameUseCase(getUserUseCase, repository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideNearbyGamesDataSource(
        @ApplicationContext context: Context,
        connectionsClient: ConnectionsClient,
        payloadCallback: PayloadCallback,
        @ServiceId serviceId: String
    ): NearbyDataSource {
        return NearbyDataSourceImpl(context, connectionsClient, payloadCallback, serviceId)
    }

    @Provides
    @Singleton
    fun provideRecentGamesDataSource(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): RecentGamesDataSource {
        return RecentGamesLocalDataSource(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideNearbyGamesRepository(
        nearbyDataSource: NearbyDataSource,
        recentGamesDataSource: RecentGamesDataSource,
        payloadCallback: PayloadCallback
    ): NearbyRepository {
        return NearbyRepositoryImpl(
            nearbyDataSource,
            recentGamesDataSource,
            payloadCallback
        )
    }
}