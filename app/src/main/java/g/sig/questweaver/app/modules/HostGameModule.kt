package g.sig.questweaver.app.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.domain.usecases.host.VerifyDescriptionUseCase
import g.sig.questweaver.domain.usecases.host.VerifyGameNameUseCase
import g.sig.questweaver.domain.usecases.host.VerifyPlayerCountUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HostGameModule {
    @Provides
    @Singleton
    fun provideVerifyDescriptionUseCase(): VerifyDescriptionUseCase = VerifyDescriptionUseCase()

    @Provides
    @Singleton
    fun provideVerifyGameNameUseCase(): VerifyGameNameUseCase = VerifyGameNameUseCase()

    @Provides
    @Singleton
    fun provideVerifyPlayerCountUseCase(): VerifyPlayerCountUseCase = VerifyPlayerCountUseCase()
}
