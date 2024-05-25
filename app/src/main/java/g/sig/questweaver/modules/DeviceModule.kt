package g.sig.questweaver.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.nearby.DeviceRepositoryImpl
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.usecases.nearby.GetDevicesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {
    @Provides
    @Singleton
    fun provideDeviceRepository(
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): DeviceRepository = DeviceRepositoryImpl(ioDispatcher)

    @Provides
    @Singleton
    fun provideGetDevicesUseCase(deviceRepository: DeviceRepository): GetDevicesUseCase = GetDevicesUseCase(deviceRepository)
}