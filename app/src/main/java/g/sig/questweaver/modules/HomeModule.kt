package g.sig.questweaver.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.domain.repositories.RecentGamesRepository
import g.sig.domain.repositories.UserRepository
import g.sig.domain.usecases.home.GetHomeUseCase
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object HomeModule {

    @Provides
    @Singleton
    fun provideGetHomeUseCase(
        getNearbyPermissionUseCase: GetNearbyPermissionUseCase,
        recentGamesRepository: RecentGamesRepository,
        userRepository: UserRepository,
        @DefaultDispatcher mainDispatcher: CoroutineDispatcher
    ): GetHomeUseCase {
        return GetHomeUseCase(getNearbyPermissionUseCase, recentGamesRepository, userRepository, mainDispatcher)
    }
}