package g.sig.questweaver.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.domain.repositories.NearbyRepository
import g.sig.domain.usecases.host.CreateGameUseCase
import g.sig.domain.usecases.host.VerifyDescriptionUseCase
import g.sig.domain.usecases.host.VerifyGameNameUseCase
import g.sig.domain.usecases.host.VerifyPlayerCountUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HostGameModule {
    @Provides
    @Singleton
    fun provideCreateGameUseCase(nearbyRepository: NearbyRepository, @DefaultDispatcher defaultDispatcher: CoroutineDispatcher): CreateGameUseCase {
        return CreateGameUseCase(nearbyRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideVerifyDescriptionUseCase(): VerifyDescriptionUseCase {
        return VerifyDescriptionUseCase()
    }

    @Provides
    @Singleton
    fun provideVerifyGameNameUseCase(): VerifyGameNameUseCase {
        return VerifyGameNameUseCase()
    }

    @Provides
    @Singleton
    fun provideVerifyPlayerCountUseCase(): VerifyPlayerCountUseCase {
        return VerifyPlayerCountUseCase()
    }
}