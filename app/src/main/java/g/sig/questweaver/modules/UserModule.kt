package g.sig.questweaver.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.user.UserDataSource
import g.sig.data.datasources.user.UserLocalDataSource
import g.sig.data.repositories.UserRepository
import g.sig.domain.user.CreateUserUseCase
import g.sig.domain.user.DeleteUserUseCase
import g.sig.domain.user.ShouldShowOnBoardingUseCase
import g.sig.domain.user.UpdateUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserModule {
    @Provides
    @Singleton
    fun providesUserLocalDataSource(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): UserDataSource {
        return UserLocalDataSource(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dataSource: UserDataSource): UserRepository {
        return UserRepository(userDataSource = dataSource)
    }

    @Provides
    @Singleton
    fun provideCreateUserUseCase(
        userRepository: UserRepository
    ): CreateUserUseCase {
        return CreateUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(
        userRepository: UserRepository
    ): DeleteUserUseCase {
        return DeleteUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(
        userRepository: UserRepository
    ): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideShouldShowOnBoardingUseCase(
        userRepository: UserRepository
    ): ShouldShowOnBoardingUseCase {
        return ShouldShowOnBoardingUseCase(userRepository)
    }

}