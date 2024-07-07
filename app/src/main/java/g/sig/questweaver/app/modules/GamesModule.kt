package g.sig.questweaver.app.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.data.datasources.game.GameDataSource
import g.sig.questweaver.data.datasources.game.GameDataSourceImpl
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.datasources.recentgames.RecentGamesLocalDataSource
import g.sig.questweaver.data.repositories.GameRepositoryImpl
import g.sig.questweaver.domain.repositories.GameRepository
import g.sig.questweaver.domain.usecases.game.CreateGameUseCase
import g.sig.questweaver.domain.usecases.game.DeleteGameUseCase
import g.sig.questweaver.domain.usecases.game.GetDmIdUseCase
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.game.HandleDmPayloadUseCase
import g.sig.questweaver.domain.usecases.game.ReconnectToGameUseCase
import g.sig.questweaver.domain.usecases.game.UpdateGameUseCase
import g.sig.questweaver.domain.usecases.game.state.CreateGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.RemoveGameStateUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.RequestConnectionUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamesModule {
    @Provides
    @Singleton
    fun provideGameSessionDataSource(): GameDataSource = GameDataSourceImpl()

    @Provides
    @Singleton
    fun provideRecentGamesDataSource(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): RecentGamesDataSource = RecentGamesLocalDataSource(context, ioDispatcher)

    @Provides
    @Singleton
    fun provideGameSessionRepository(
        gameDataSource: GameDataSource,
        recentGamesDataSource: RecentGamesDataSource,
    ): GameRepository = GameRepositoryImpl(gameDataSource, recentGamesDataSource)

    @Provides
    @Singleton
    fun provideCreateGameSessionUseCase(
        getUserUseCase: GetUserUseCase,
        getGameStateUseCase: GetGameStateUseCase,
        createGameStateUseCase: CreateGameStateUseCase,
        gameRepository: GameRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CreateGameUseCase =
        CreateGameUseCase(
            getUserUseCase,
            getGameStateUseCase,
            createGameStateUseCase,
            gameRepository,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideDeleteGameSessionUseCase(
        gameRepository: GameRepository,
        removeGameStateUseCase: RemoveGameStateUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): DeleteGameUseCase = DeleteGameUseCase(gameRepository, removeGameStateUseCase, defaultDispatcher)

    @Provides
    @Singleton
    fun provideUpdateGameSessionUseCase(
        gameRepository: GameRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): UpdateGameUseCase = UpdateGameUseCase(gameRepository, defaultDispatcher)

    @Provides
    @Singleton
    fun provideGetDmIdUseCase(
        gameRepository: GameRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): GetDmIdUseCase = GetDmIdUseCase(gameRepository, defaultDispatcher)

    @Provides
    @Singleton
    fun provideHandleDmPayloadUseCase(
        getUserUseCase: GetUserUseCase,
        getDmIdUseCase: GetDmIdUseCase,
        broadcastPayloadUseCase: BroadcastPayloadUseCase,
        sendPayloadUseCase: SendPayloadUseCase,
        getGameStateUseCase: GetGameStateUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): HandleDmPayloadUseCase =
        HandleDmPayloadUseCase(
            getUserUseCase,
            getDmIdUseCase,
            broadcastPayloadUseCase,
            sendPayloadUseCase,
            getGameStateUseCase,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideReconnectToGameUseCase(
        requestConnectionUseCase: RequestConnectionUseCase,
        gameRepository: GameRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): ReconnectToGameUseCase =
        ReconnectToGameUseCase(
            requestConnectionUseCase,
            gameRepository,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideGetGameUseCase(
        gameRepository: GameRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): GetGameUseCase = GetGameUseCase(gameRepository, defaultDispatcher)
}
