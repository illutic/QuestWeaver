package g.sig.questweaver.app.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.data.datasources.nearby.DeviceRepositoryImpl
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.usecases.device.GetConnectedDeviceUseCase
import g.sig.questweaver.domain.usecases.device.GetDevicesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {
    @Provides
    @Singleton
    fun provideDeviceRepository(
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): DeviceRepository = DeviceRepositoryImpl(ioDispatcher)

    @Provides
    @Singleton
    fun provideGetDevicesUseCase(deviceRepository: DeviceRepository): GetDevicesUseCase =
        GetDevicesUseCase(
            deviceRepository,
        )

    @Provides
    @Singleton
    fun provideGetConnectedDeviceUseCase(deviceRepository: DeviceRepository): GetConnectedDeviceUseCase =
        GetConnectedDeviceUseCase(deviceRepository)
}
