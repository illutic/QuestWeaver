package g.sig.questweaver.app.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.data.datasources.user.UserDataSource
import g.sig.questweaver.data.datasources.user.UserLocalDataSource
import g.sig.questweaver.data.repositories.UserRepositoryImpl
import g.sig.questweaver.domain.repositories.UserRepository
import g.sig.questweaver.domain.usecases.user.CreateUserUseCase
import g.sig.questweaver.domain.usecases.user.DeleteUserUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import g.sig.questweaver.domain.usecases.user.HasUserUseCase
import g.sig.questweaver.domain.usecases.user.UpdateUserNameUseCase
import g.sig.questweaver.domain.usecases.user.ValidateUserNameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserModule {
    @Provides
    @Singleton
    fun provideUserLocalDataSource(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): UserDataSource {
        return UserLocalDataSource(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource = dataSource)
    }

    @Provides
    @Singleton
    fun providesGetUserUseCase(
        userRepository: UserRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): GetUserUseCase {
        return GetUserUseCase(userRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideCreateUserUseCase(
        userRepository: UserRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CreateUserUseCase {
        return CreateUserUseCase(userRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(
        userRepository: UserRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): DeleteUserUseCase {
        return DeleteUserUseCase(userRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(
        userRepository: UserRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): UpdateUserNameUseCase {
        return UpdateUserNameUseCase(userRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideShouldShowOnBoardingUseCase(
        userRepository: UserRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): HasUserUseCase {
        return HasUserUseCase(userRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideValidateUserUseCase(): ValidateUserNameUseCase {
        return ValidateUserNameUseCase()
    }
}
