package g.sig.questweaver.app.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.data.datasources.gamestate.GameStateDataSource
import g.sig.questweaver.data.datasources.gamestate.GameStateDataSourceImpl
import g.sig.questweaver.data.datasources.gamestate.GameStatesDataStore
import g.sig.questweaver.data.repositories.GameStateRepositoryImpl
import g.sig.questweaver.domain.repositories.GameStateRepository
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.game.UpdateGameUseCase
import g.sig.questweaver.domain.usecases.game.state.CreateGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.CreateOrUpdateGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.LoadGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.RemoveGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.UpdateGameStateUseCase
import g.sig.questweaver.domain.usecases.nearby.OnPayloadReceivedUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameStateModule {
    @Provides
    @Singleton
    fun provideGameStateDataSource(
        @ApplicationContext context: Context,
    ): GameStateDataSource = GameStateDataSourceImpl(GameStatesDataStore(context))

    @Provides
    @Singleton
    fun provideGameStateRepository(gameStateDataSource: GameStateDataSource): GameStateRepository =
        GameStateRepositoryImpl(gameStateDataSource)

    @Provides
    @Singleton
    fun provideCreateGameStateUseCase(
        getGameUseCase: GetGameUseCase,
        gameStateRepository: GameStateRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CreateGameStateUseCase =
        CreateGameStateUseCase(
            getGameUseCase,
            gameStateRepository,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideGetGameStateUseCase(
        getGameUseCase: GetGameUseCase,
        gameStateRepository: GameStateRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): GetGameStateUseCase =
        GetGameStateUseCase(
            getGameUseCase,
            gameStateRepository,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideRemoveGameStateUseCase(
        gameStateRepository: GameStateRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): RemoveGameStateUseCase =
        RemoveGameStateUseCase(
            gameStateRepository,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideUpdateGameStateUseCase(
        getGameStateUseCase: GetGameStateUseCase,
        gameStateRepository: GameStateRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): UpdateGameStateUseCase =
        UpdateGameStateUseCase(
            getGameStateUseCase,
            gameStateRepository,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideCreateOrUpdateGameStateUseCase(
        getGameUseCase: GetGameUseCase,
        createGameStateUseCase: CreateGameStateUseCase,
        updateGameStateUseCase: UpdateGameStateUseCase,
        getGameStateUseCase: GetGameStateUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CreateOrUpdateGameStateUseCase =
        CreateOrUpdateGameStateUseCase(
            getGameUseCase,
            createGameStateUseCase,
            updateGameStateUseCase,
            getGameStateUseCase,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideLoadGameStateUseCase(
        getGameUseCase: GetGameUseCase,
        getUserUseCase: GetUserUseCase,
        getGameStateUseCase: GetGameStateUseCase,
        sendPayloadUseCase: SendPayloadUseCase,
        onPayloadReceived: OnPayloadReceivedUseCase,
        createOrUpdateGameStateUseCase: CreateOrUpdateGameStateUseCase,
        updateGameUseCase: UpdateGameUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): LoadGameStateUseCase =
        LoadGameStateUseCase(
            getGameUseCase = getGameUseCase,
            getUserUseCase = getUserUseCase,
            getGameStateUseCase = getGameStateUseCase,
            sendPayloadUseCase = sendPayloadUseCase,
            onPayloadReceived = onPayloadReceived,
            createOrUpdateGameStateUseCase = createOrUpdateGameStateUseCase,
            updateGameUseCase = updateGameUseCase,
            defaultDispatcher = defaultDispatcher,
        )
}
