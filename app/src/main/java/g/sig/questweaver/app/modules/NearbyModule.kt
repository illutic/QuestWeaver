package g.sig.questweaver.app.modules

import com.google.android.gms.nearby.connection.ConnectionsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.data.datasources.nearby.NearbyDataSource
import g.sig.questweaver.data.datasources.nearby.NearbyDataSourceImpl
import g.sig.questweaver.data.datasources.nearby.PayloadCallback
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.repositories.NearbyRepositoryImpl
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
import g.sig.questweaver.domain.repositories.PayloadRepository
import g.sig.questweaver.domain.usecases.game.HandleDmPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.AdvertiseGameUseCase
import g.sig.questweaver.domain.usecases.nearby.CancelAdvertisementUseCase
import g.sig.questweaver.domain.usecases.nearby.CancelDiscoveryUseCase
import g.sig.questweaver.domain.usecases.nearby.DiscoverNearbyDevicesUseCase
import g.sig.questweaver.domain.usecases.nearby.OnPayloadReceivedUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NearbyModule {
    @Provides
    @Singleton
    fun provideAdvertiseGameUseCase(
        nearbyRepository: NearbyRepository,
        deviceRepository: DeviceRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): AdvertiseGameUseCase = AdvertiseGameUseCase(nearbyRepository, deviceRepository, defaultDispatcher)

    @Provides
    @Singleton
    fun provideCancelAdvertisementGameUseCase(nearbyRepository: NearbyRepository): CancelAdvertisementUseCase =
        CancelAdvertisementUseCase(nearbyRepository)

    @Provides
    @Singleton
    fun provideCancelDiscoveryUseCase(nearbyRepository: NearbyRepository): CancelDiscoveryUseCase = CancelDiscoveryUseCase(nearbyRepository)

    @Provides
    @Singleton
    fun provideNearbyGamesRepository(
        nearbyDataSource: NearbyDataSource,
        recentGamesDataSource: RecentGamesDataSource,
    ): NearbyRepository =
        NearbyRepositoryImpl(
            nearbyDataSource,
            recentGamesDataSource,
        )

    @Provides
    @Singleton
    fun provideGetNearbyGamesUseCase(
        repository: NearbyRepository,
        deviceRepository: DeviceRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): DiscoverNearbyDevicesUseCase =
        DiscoverNearbyDevicesUseCase(
            repository,
            deviceRepository,
            getUserUseCase,
            defaultDispatcher,
        )

    @Provides
    @Singleton
    fun provideNearbyGamesDataSource(
        connectionsClient: ConnectionsClient,
        payloadCallback: PayloadCallback,
        @ServiceId serviceId: String,
    ): NearbyDataSource =
        NearbyDataSourceImpl(
            connectionsClient,
            payloadCallback,
            serviceId,
        )

    @Provides
    @Singleton
    fun provideOnPayloadReceivedUseCase(
        handleDmPayloadUseCase: HandleDmPayloadUseCase,
        payloadRepository: PayloadRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): OnPayloadReceivedUseCase =
        OnPayloadReceivedUseCase(
            handleDmPayloadUseCase,
            payloadRepository,
            defaultDispatcher,
        )
}
