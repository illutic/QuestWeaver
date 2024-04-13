package g.sig.questweaver.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.game.GameSessionDataSource
import g.sig.data.datasources.game.GameSessionDataSourceImpl
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.datasources.recentgames.RecentGamesLocalDataSource
import g.sig.data.repositories.GameSessionRepositoryImpl
import g.sig.domain.repositories.GameSessionRepository
import g.sig.domain.usecases.host.CreateGameSessionUseCase
import g.sig.domain.usecases.host.DeleteGameSessionUseCase
import g.sig.domain.usecases.host.GetGameSessionUseCase
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
    fun provideRecentGamesDataSource(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): RecentGamesDataSource {
        return RecentGamesLocalDataSource(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideGameSessionRepository(
        gameSessionDataSource: GameSessionDataSource
    ): GameSessionRepository {
        return GameSessionRepositoryImpl(gameSessionDataSource)
    }

    @Provides
    @Singleton
    fun provideCreateGameSessionUseCase(
        gameSessionRepository: GameSessionRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CreateGameSessionUseCase {
        return CreateGameSessionUseCase(gameSessionRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideDeleteGameSessionUseCase(
        gameSessionRepository: GameSessionRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): DeleteGameSessionUseCase {
        return DeleteGameSessionUseCase(gameSessionRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideGetGameSessionUseCase(
        gameSessionRepository: GameSessionRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): GetGameSessionUseCase {
        return GetGameSessionUseCase(gameSessionRepository, defaultDispatcher)
    }
}