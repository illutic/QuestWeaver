package g.sig.questweaver.app.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.data.datasources.game.GameSessionDataSource
import g.sig.questweaver.data.datasources.game.GameSessionDataSourceImpl
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.datasources.recentgames.RecentGamesLocalDataSource
import g.sig.questweaver.data.repositories.GameSessionRepositoryImpl
import g.sig.questweaver.domain.repositories.GameSessionRepository
import g.sig.questweaver.domain.usecases.game.CreateGameSessionUseCase
import g.sig.questweaver.domain.usecases.game.DeleteGameSessionUseCase
import g.sig.questweaver.domain.usecases.game.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.game.UpdateGameSessionUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
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
        gameSessionDataSource: GameSessionDataSource,
        recentGamesDataSource: RecentGamesDataSource
    ): GameSessionRepository {
        return GameSessionRepositoryImpl(gameSessionDataSource, recentGamesDataSource)
    }

    @Provides
    @Singleton
    fun provideCreateGameSessionUseCase(
        getUserUseCase: GetUserUseCase,
        gameSessionRepository: GameSessionRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CreateGameSessionUseCase {
        return CreateGameSessionUseCase(
            getUserUseCase,
            gameSessionRepository,
            defaultDispatcher
        )
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
    ): GetGameStateUseCase {
        return GetGameStateUseCase(gameSessionRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideUpdateGameSessionUseCase(
        gameSessionRepository: GameSessionRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): UpdateGameSessionUseCase {
        return UpdateGameSessionUseCase(gameSessionRepository, defaultDispatcher)
    }
}
