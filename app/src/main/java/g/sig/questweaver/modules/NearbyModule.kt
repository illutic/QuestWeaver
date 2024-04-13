package g.sig.questweaver.modules

import com.google.android.gms.nearby.connection.ConnectionsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.nearby.DeviceRepositoryImpl
import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.nearby.NearbyDataSourceImpl
import g.sig.data.datasources.nearby.PayloadCallback
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.repositories.NearbyRepositoryImpl
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.NearbyRepository
import g.sig.domain.usecases.nearby.AdvertiseGameUseCase
import g.sig.domain.usecases.nearby.CancelAdvertisementGameUseCase
import g.sig.domain.usecases.nearby.DiscoverNearbyDevicesUseCase
import g.sig.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NearbyModule {
    @Provides
    @Singleton
    fun provideAdvertiseGameUseCase(nearbyRepository: NearbyRepository, @DefaultDispatcher defaultDispatcher: CoroutineDispatcher): AdvertiseGameUseCase {
        return AdvertiseGameUseCase(nearbyRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideCancelAdvertisementGameUseCase(
        nearbyRepository: NearbyRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CancelAdvertisementGameUseCase {
        return CancelAdvertisementGameUseCase(nearbyRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideNearbyGamesRepository(
        deviceRepository: DeviceRepository,
        nearbyDataSource: NearbyDataSource,
        recentGamesDataSource: RecentGamesDataSource
    ): NearbyRepository {
        return NearbyRepositoryImpl(
            deviceRepository,
            nearbyDataSource,
            recentGamesDataSource
        )
    }

    @Provides
    @Singleton
    fun provideGetNearbyGamesUseCase(
        repository: NearbyRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): DiscoverNearbyDevicesUseCase {
        return DiscoverNearbyDevicesUseCase(repository, getUserUseCase, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideNearbyGamesDataSource(
        connectionsClient: ConnectionsClient,
        payloadCallback: PayloadCallback,
        @ServiceId serviceId: String
    ): NearbyDataSource {
        return NearbyDataSourceImpl(connectionsClient, payloadCallback, serviceId)
    }

    @Provides
    @Singleton
    fun provideDeviceRepository(): DeviceRepository {
        return DeviceRepositoryImpl()
    }
}