package g.sig.questweaver.app.modules

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.questweaver.app.data.CorePayloadCallback
import g.sig.questweaver.data.datasources.nearby.PayloadCallback
import g.sig.questweaver.data.repositories.PayloadRepositoryImpl
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
import g.sig.questweaver.domain.repositories.PayloadRepository
import g.sig.questweaver.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.CancelDiscoveryUseCase
import g.sig.questweaver.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.questweaver.domain.usecases.nearby.RequestConnectionUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionsClient {

    @Provides
    @Singleton
    @ServiceId
    fun provideServiceId(@ApplicationContext context: Context): String = context.packageName

    @Provides
    @Singleton
    fun provideRequestConnectionUseCase(
        cancelDiscoveryUseCase: CancelDiscoveryUseCase,
        nearbyRepository: NearbyRepository,
        deviceRepository: DeviceRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): RequestConnectionUseCase {
        return RequestConnectionUseCase(
            cancelDiscoveryUseCase,
            nearbyRepository,
            deviceRepository,
            getUserUseCase,
            defaultDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideAcceptConnectionUseCase(
        nearbyRepository: NearbyRepository,
        deviceRepository: DeviceRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): AcceptConnectionUseCase {
        val connectedDeviceScope =
            CoroutineScope(defaultDispatcher + CoroutineName("AcceptConnectionScope"))
        return AcceptConnectionUseCase(nearbyRepository, deviceRepository, connectedDeviceScope)
    }

    @Provides
    @Singleton
    fun provideRejectConnectionUseCase(
        deviceRepository: DeviceRepository,
        nearbyRepository: NearbyRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): RejectConnectionUseCase {
        return RejectConnectionUseCase(deviceRepository, nearbyRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun providePayloadCallback(
        @ApplicationContext context: Context,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): PayloadCallback {
        return CorePayloadCallback(context, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideConnectionsClient(
        @ApplicationContext context: Context
    ): ConnectionsClient {
        return Nearby.getConnectionsClient(context)
    }

    @Provides
    @Singleton
    fun providePayloadRepository(
        @ApplicationContext context: Context,
        connectionsClient: ConnectionsClient,
        payloadCallback: PayloadCallback
    ): PayloadRepository {
        return PayloadRepositoryImpl(context, connectionsClient, payloadCallback)
    }

    @Provides
    @Singleton
    fun provideSendPayloadUseCase(
        payloadRepository: PayloadRepository
    ): SendPayloadUseCase {
        return SendPayloadUseCase(payloadRepository)
    }

    @Provides
    @Singleton
    fun provideBroadcastPayloadUseCase(
        payloadRepository: PayloadRepository,
        deviceRepository: DeviceRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): BroadcastPayloadUseCase {
        return BroadcastPayloadUseCase(payloadRepository, deviceRepository, defaultDispatcher)
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ServiceId
