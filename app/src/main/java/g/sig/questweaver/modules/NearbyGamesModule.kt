package g.sig.questweaver.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.nearby.NearbyGameDataSource
import g.sig.data.datasources.nearby.NearbyGameDataSourceImpl
import g.sig.data.repositories.NearbyGamesRepositoryImpl
import g.sig.domain.repositories.NearbyGamesRepository
import g.sig.domain.usecases.nearby.GetNearbyGamesUseCase
import g.sig.domain.usecases.nearby.JoinGameUseCase
import g.sig.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NearbyGamesModule {

    @Provides
    @Singleton
    fun provideGetNearbyGamesUseCase(repository: NearbyGamesRepository, @DefaultDispatcher defaultDispatcher: CoroutineDispatcher): GetNearbyGamesUseCase {
        return GetNearbyGamesUseCase(repository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideJoinGameUseCase(
        repository: NearbyGamesRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): JoinGameUseCase {
        return JoinGameUseCase(getUserUseCase, repository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideNearbyGamesDataSource(@ApplicationContext context: Context): NearbyGameDataSource {
        return NearbyGameDataSourceImpl(context)
    }

    @Provides
    @Singleton
    fun provideNearbyGamesRepository(dataSource: NearbyGameDataSource): NearbyGamesRepository {
        return NearbyGamesRepositoryImpl(dataSource)
    }
}