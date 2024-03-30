package g.sig.questweaver.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.domain.repositories.RemoteConfigRepository
import g.sig.domain.usecases.remoteconfig.FetchRemoteConfigValueUseCase
import g.sig.questweaver.remoteconfig.RemoteConfigRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Provides
    @Singleton
    fun provideRemoteConfigRepository(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl().also {
            CoroutineScope(defaultDispatcher).launch {
                it.fetchRemoteConfig()
            }
        }
    }

    @Provides
    @Singleton
    fun provideRemoteConfigValueUseCase(
        remoteConfigRepository: RemoteConfigRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): FetchRemoteConfigValueUseCase {
        return FetchRemoteConfigValueUseCase(remoteConfigRepository, defaultDispatcher)
    }
}