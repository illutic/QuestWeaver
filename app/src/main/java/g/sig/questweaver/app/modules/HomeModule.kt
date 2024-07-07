package g.sig.questweaver.app.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.domain.repositories.NearbyRepository
import g.sig.questweaver.domain.repositories.UserRepository
import g.sig.questweaver.domain.usecases.home.GetHomeUseCase
import g.sig.questweaver.domain.usecases.permissions.GetNearbyPermissionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HomeModule {
    @Provides
    @Singleton
    fun provideGetHomeUseCase(
        getNearbyPermissionUseCase: GetNearbyPermissionUseCase,
        nearbyRepository: NearbyRepository,
        userRepository: UserRepository,
        @DefaultDispatcher mainDispatcher: CoroutineDispatcher,
    ): GetHomeUseCase =
        GetHomeUseCase(
            getNearbyPermissionUseCase,
            nearbyRepository,
            userRepository,
            mainDispatcher,
        )
}
