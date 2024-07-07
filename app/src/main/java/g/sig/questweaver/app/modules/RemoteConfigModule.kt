package g.sig.questweaver.app.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.app.remoteconfig.RemoteConfigRepositoryImpl
import g.sig.questweaver.domain.repositories.RemoteConfigRepository
import g.sig.questweaver.domain.usecases.remoteconfig.FetchRemoteConfigValueUseCase
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
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): RemoteConfigRepository =
        RemoteConfigRepositoryImpl().also {
            CoroutineScope(defaultDispatcher).launch {
                it.fetchRemoteConfig()
            }
        }

    @Provides
    @Singleton
    fun provideRemoteConfigValueUseCase(
        remoteConfigRepository: RemoteConfigRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): FetchRemoteConfigValueUseCase = FetchRemoteConfigValueUseCase(remoteConfigRepository, defaultDispatcher)
}
