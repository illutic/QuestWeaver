package g.sig.questweaver.modules

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g.sig.data.datasources.nearby.PayloadCallback
import g.sig.data.repositories.PayloadRepositoryImpl
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.NearbyRepository
import g.sig.domain.repositories.PayloadRepository
import g.sig.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.domain.usecases.nearby.RequestConnectionUseCase
import g.sig.domain.usecases.nearby.SendPayloadUseCase
import g.sig.domain.usecases.user.GetUserUseCase
import g.sig.questweaver.data.CorePayloadCallback
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionsClient {

    @Provides
    @Singleton
    @ServiceId
    fun provideServiceId(@ApplicationContext context: Context) = context.packageName

    @Provides
    @Singleton
    fun provideRequestConnectionUseCase(
        nearbyRepository: NearbyRepository,
        deviceRepository: DeviceRepository,
        getUserUseCase: GetUserUseCase,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): RequestConnectionUseCase {
        return RequestConnectionUseCase(nearbyRepository, deviceRepository, getUserUseCase, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideAcceptConnectionUseCase(
        nearbyRepository: NearbyRepository,
        deviceRepository: DeviceRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): AcceptConnectionUseCase {
        return AcceptConnectionUseCase(nearbyRepository, deviceRepository, defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideRejectConnectionUseCase(
        nearbyRepository: NearbyRepository,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): RejectConnectionUseCase {
        return RejectConnectionUseCase(nearbyRepository, defaultDispatcher)
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